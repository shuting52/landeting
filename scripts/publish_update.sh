#!/usr/bin/env bash
# ============================================================
# 懒得听「自动更新发布脚本」（raw 直链版）
# 用法:
#   ./scripts/publish_update.sh <APK路径> <versionCode> <versionName> [更新说明...]
# 示例:
#   ./scripts/publish_update.sh dist/landeting-v1.0.2.apk 3 1.0.2 "新增功能" "修复问题"
# 功能:
#   1. 校验 APK 元数据 (aapt)
#   2. 将 APK 提交到仓库 dist/ 目录并推送 main 分支
#   3. 自动生成 update.json（downloadUrl 为 raw.githubusercontent.com 直链）
#   4. 连同 update.json 一起推送
#   手机端 AppUpdateChecker 检测到 versionCode 增大即自动弹窗 -> raw 直连下载 -> 安装
# 注意: APK 大小需 < 100MB（GitHub raw 单文件限制），当前约 20MB 无压力
# ============================================================
set -euo pipefail

APK_PATH="${1:?用法: publish_update.sh <APK路径> <versionCode> <versionName> [说明...]}"
VERSION_CODE="${2:?缺少 versionCode}"
VERSION_NAME="${3:?缺少 versionName}"
shift 3
RELEASE_NOTES=("$@")

GH_TOKEN="${GH_TOKEN:-}"
REPO_OWNER="${REPO_OWNER:-shuting52}"
REPO_NAME="${REPO_NAME:-landeting}"
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"

if [ -z "$GH_TOKEN" ]; then
  echo "!! 请设置 GH_TOKEN 环境变量 (GitHub 访问令牌)"
  exit 1
fi

# ---------- 校验 APK ----------
AAPT_BIN="$(command -v aapt 2>/dev/null || echo "${ANDROID_HOME:-}/build-tools/36.0.0/aapt")"
if [ -x "$AAPT_BIN" ]; then
  echo "==> 校验 APK 元数据..."
  "$AAPT_BIN" dump badging "$APK_PATH" 2>&1 | grep -E "package:|launchable-activity" | head -3
fi

APK_SIZE=$(stat -c%s "$APK_PATH")
APK_BASENAME=$(basename "$APK_PATH")
RAW_BASE="https://raw.githubusercontent.com/${REPO_OWNER}/${REPO_NAME}/main"

if [ "$APK_SIZE" -gt 104857600 ]; then
  echo "!! APK 超过 100MB，GitHub raw 无法直链，请改用 GitHub Release 方案"
  exit 1
fi

echo "==> 发布 ${APK_BASENAME} (versionCode=${VERSION_CODE} / v${VERSION_NAME}, ${APK_SIZE} bytes)"

# ---------- 将 APK 放入仓库 dist/ 目录（raw 直链的前提）----------
DIST_DIR="${ROOT_DIR}/dist"
mkdir -p "$DIST_DIR"
DIST_APK="${DIST_DIR}/${APK_BASENAME}"
cp -f "$APK_PATH" "$DIST_APK"
DOWNLOAD_URL="${RAW_BASE}/dist/${APK_BASENAME}"
echo "==> APK 已放入仓库: ${DIST_APK}"
echo "==> raw 下载地址: ${DOWNLOAD_URL}"

# ---------- 生成 update.json ----------
NOTES_JSON=$(python3 - "${RELEASE_NOTES[@]}" <<'PYEOF'
import json, sys
notes = [n for n in sys.argv[1:] if n.strip()]
if not notes:
    notes = ["优化使用体验，修复已知问题"]
print(json.dumps(notes, ensure_ascii=False))
PYEOF
)
python3 - "$VERSION_CODE" "$VERSION_NAME" "$DOWNLOAD_URL" "$APK_SIZE" "$NOTES_JSON" <<'PYEOF' > "${ROOT_DIR}/update.json"
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
echo "==> update.json 已生成:"
cat "${ROOT_DIR}/update.json"

# ---------- 推送 APK + update.json ----------
cd "$ROOT_DIR"
git add dist/*.apk update.json
git -c user.name="${REPO_OWNER}" -c user.email="${REPO_OWNER}@users.noreply.github.com" \
  commit -m "release: v${VERSION_NAME} (versionCode ${VERSION_CODE}, raw 直链)" 2>/dev/null || true
git push "https://x-access-token:${GH_TOKEN}@github.com/${REPO_OWNER}/${REPO_NAME}.git" main

echo ""
echo "✅ 发布完成！"
echo "   新版本: v${VERSION_NAME} (versionCode ${VERSION_CODE})"
echo "   更新清单: ${RAW_BASE}/update.json"
echo "   APK 直链: ${DOWNLOAD_URL}"
echo "   说明: raw 直链有 CDN 缓存，发布后 5 分钟内旧缓存可能仍在，属正常现象"
