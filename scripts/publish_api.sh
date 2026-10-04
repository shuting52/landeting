#!/usr/bin/env bash
# ============================================================
# 懒得听「自动更新发布脚本」（GitHub API 版）
# 用法:
#   ./scripts/publish_api.sh <APK路径> <versionCode> <versionName> [更新说明...]
# 说明:
#   - 通过 GitHub API (Git Data) 提交 APK + update.json 到 main 分支
#   - 避免 git push 在 Actions 环境中凭证被替换为 GITHUB_TOKEN 的问题
#   - APK 大小需 < 100MB（GitHub blob 上限）
# ============================================================
set -euo pipefail

APK_PATH="${1:?用法: publish_api.sh <APK路径> <versionCode> <versionName> [说明...]}"
VERSION_CODE="${2:?缺少 versionCode}"
VERSION_NAME="${3:?缺少 versionName}"
shift 3
RELEASE_NOTES=("$@")

GH_TOKEN="${GH_TOKEN:-}"
REPO_OWNER="${REPO_OWNER:-shuting52}"
REPO_NAME="${REPO_NAME:-landeting}"
API="https://api.github.com/repos/${REPO_OWNER}/${REPO_NAME}"

if [ -z "$GH_TOKEN" ]; then
  echo "!! 请设置 GH_TOKEN 环境变量 (GitHub 访问令牌)"
  exit 1
fi

APK_SIZE=$(stat -c%s "$APK_PATH")
APK_BASENAME=$(basename "$APK_PATH")
RAW_BASE="https://raw.githubusercontent.com/${REPO_OWNER}/${REPO_NAME}/main"

if [ "$APK_SIZE" -gt 104857600 ]; then
  echo "!! APK 超过 100MB，GitHub blob 无法承载，请改用 Release 方案"
  exit 1
fi

echo "==> 发布 ${APK_BASENAME} (versionCode=${VERSION_CODE} / v${VERSION_NAME}, ${APK_SIZE} bytes)"
DOWNLOAD_URL="${RAW_BASE}/dist/${APK_BASENAME}"

# ---------- 生成 update.json ----------
NOTES_JSON=$(python3 - "${RELEASE_NOTES[@]}" <<'PYEOF'
import json, sys
notes = [n for n in sys.argv[1:] if n.strip()]
if not notes:
    notes = ["优化使用体验，修复已知问题"]
print(json.dumps(notes, ensure_ascii=False))
PYEOF
)
UPDATE_JSON=$(python3 - "$VERSION_CODE" "$VERSION_NAME" "$DOWNLOAD_URL" "$APK_SIZE" "$NOTES_JSON" <<'PYEOF'
import json, sys
vc, vn, url, size, notes = int(sys.argv[1]), sys.argv[2], sys.argv[3], int(sys.argv[4]), json.loads(sys.argv[5])
info = {
    "versionCode": vc,
    "versionName": vn,
    "downloadUrl": url,
    "apkSize": size,
    "forceUpdate": False,
    "md5": "",
    "releaseNotes": notes,
}
print(json.dumps(info, ensure_ascii=False, indent=2))
PYEOF
)
echo "==> update.json:"
echo "$UPDATE_JSON"

# ---------- 1. 获取当前 main HEAD ----------
echo "==> 获取 main HEAD..."
HEAD_SHA=$(curl -s -H "Authorization: token ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  "${API}/git/ref/heads/main" | python3 -c "import json,sys; print(json.load(sys.stdin)['object']['sha'])")
echo "    HEAD: ${HEAD_SHA}"

# ---------- 2. 创建 APK blob ----------
echo "==> 创建 APK blob (${APK_SIZE} bytes)..."
python3 -c "import base64,sys; print(base64.b64encode(open('$APK_PATH','rb').read()).decode())" > /tmp/publish_apk_b64.txt
python3 - <<'PYEOF' > /tmp/publish_blob_body.json
import json
b64 = open('/tmp/publish_apk_b64.txt').read().strip()
print(json.dumps({"content": b64, "encoding": "base64"}))
PYEOF
APK_SHA=$(curl -s -X POST -H "Authorization: token ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  -H "Content-Type: application/json" --data-binary @/tmp/publish_blob_body.json \
  "${API}/git/blobs" | python3 -c "import json,sys; print(json.load(sys.stdin)['sha'])")
echo "    APK blob: ${APK_SHA}"

# ---------- 3. 创建 update.json blob ----------
echo "==> 创建 update.json blob..."
python3 - "$UPDATE_JSON" <<'PYEOF' > /tmp/publish_update_body.json
import json, base64, sys
uj = sys.argv[1]
print(json.dumps({"content": base64.b64encode(uj.encode()).decode(), "encoding": "base64"}))
PYEOF
UPDATE_SHA=$(curl -s -X POST -H "Authorization: token ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  -H "Content-Type: application/json" --data-binary @/tmp/publish_update_body.json \
  "${API}/git/blobs" | python3 -c "import json,sys; print(json.load(sys.stdin)['sha'])")
echo "    update.json blob: ${UPDATE_SHA}"

# ---------- 4. 创建 tree（基于当前 main tree）----------
echo "==> 创建 tree..."
BASE_TREE=$(curl -s -H "Authorization: token ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  "${API}/git/commits/${HEAD_SHA}" | python3 -c "import json,sys; print(json.load(sys.stdin)['tree']['sha'])")
python3 - "$BASE_TREE" "$APK_SHA" "$UPDATE_SHA" "$APK_BASENAME" <<'PYEOF' > /tmp/publish_tree_body.json
import json, sys
base_tree, apk_sha, update_sha, apk_name = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]
tree = {
    "base_tree": base_tree,
    "tree": [
        {"path": f"dist/{apk_name}", "mode": "100644", "type": "blob", "sha": apk_sha},
        {"path": "update.json", "mode": "100644", "type": "blob", "sha": update_sha}
    ]
}
print(json.dumps(tree))
PYEOF
NEW_TREE=$(curl -s -X POST -H "Authorization: token ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  -H "Content-Type: application/json" --data-binary @/tmp/publish_tree_body.json \
  "${API}/git/trees" | python3 -c "import json,sys; print(json.load(sys.stdin)['sha'])")
echo "    tree: ${NEW_TREE}"

# ---------- 5. 创建 commit ----------
echo "==> 创建 commit..."
python3 - "$NEW_TREE" "$HEAD_SHA" "$VERSION_CODE" "$VERSION_NAME" <<'PYEOF' > /tmp/publish_commit_body.json
import json, sys
tree, parent, vc, vn = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]
commit = {
    "message": f"release: v{vn} 发布 (versionCode {vc}, CI 构建签名 APK, raw 直链)",
    "tree": tree,
    "parents": [parent],
    "author": {"name": "shuting52", "email": "shuting52@users.noreply.github.com"},
    "committer": {"name": "shuting52", "email": "shuting52@users.noreply.github.com"}
}
print(json.dumps(commit))
PYEOF
COMMIT_SHA=$(curl -s -X POST -H "Authorization: token ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  -H "Content-Type: application/json" --data-binary @/tmp/publish_commit_body.json \
  "${API}/git/commits" | python3 -c "import json,sys; print(json.load(sys.stdin)['sha'])")
echo "    commit: ${COMMIT_SHA}"

# ---------- 6. 更新 main ref ----------
echo "==> 更新 main 分支..."
curl -s -X PATCH -H "Authorization: token ${GH_TOKEN}" -H "Accept: application/vnd.github+json" \
  -H "Content-Type: application/json" \
  -d "{\"sha\": \"${COMMIT_SHA}\", \"force\": false}" \
  "${API}/git/refs/heads/main" | python3 -c "import json,sys; d=json.load(sys.stdin); print('    main ->', d['object']['sha'])"

echo ""
echo "✅ 发布完成！"
echo "   新版本: v${VERSION_NAME} (versionCode ${VERSION_CODE})"
echo "   更新清单: ${RAW_BASE}/update.json"
echo "   APK 直链: ${DOWNLOAD_URL}"
echo "   说明: raw 直链有 CDN 缓存，发布后 5 分钟内旧缓存可能仍在，属正常现象"
