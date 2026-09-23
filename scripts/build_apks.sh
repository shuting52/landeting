#!/usr/bin/env bash
# ============================================================
# 懒得听「一键构建脚本」
# 构建 v1.0.0 (旧版) 与 v1.0.1 (新版) 两个 Release APK，
# 并内置真实的远端更新检查地址 (UPDATE_CHECK_URL)。
#
# 用法:
#   export KEYSTORE_PASS=你的密钥库密码  # 可省略，默认 landeting123
#   ./scripts/build_apks.sh
# 产物:
#   dist/landeting-v1.0.0-release.apk  (versionCode=1, 旧版, 内置更新检查)
#   dist/landeting-v1.0.1-release.apk  (versionCode=2, 新版)
# ============================================================
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

# ---------- 环境 ----------
BT="${ANDROID_TOOLS:-$HOME/tools}"
GRADLE_HOME="${GRADLE_HOME:-$BT/gradle-9.3.1}"
JAVA_HOME="${JAVA_HOME:-$BT/zulu21.52.203-ca-crac-jdk21.0.12.1-linux_x64}"
ANDROID_HOME="${ANDROID_HOME:-$BT/sdk}"
export JAVA_HOME ANDROID_HOME GRADLE_USER_HOME="${GRADLE_USER_HOME:-$BT/gradle-home}"
export PATH="$JAVA_HOME/bin:$PATH"

STORE_PASSWORD="${KEYSTORE_PASS:-landeting123}"
KEY_PASSWORD="${KEYSTORE_PASS:-landeting123}"
export STORE_PASSWORD KEY_PASSWORD

UPDATE_CHECK_URL="https://raw.githubusercontent.com/shuting52/landeting/main/update.json"

mkdir -p dist

echo "========== 构建 v1.0.0 (versionCode=1) =========="
APP_VERSION_CODE=1 APP_VERSION_NAME=1.0.0 UPDATE_CHECK_URL="$UPDATE_CHECK_URL" \
  "$GRADLE_HOME/bin/gradle" :app:assembleRelease --no-daemon -Dorg.gradle.jvmargs="-Xmx3g"
cp app/build/outputs/apk/release/app-release.apk dist/landeting-v1.0.0-release.apk

echo "========== 构建 v1.0.1 (versionCode=2) =========="
APP_VERSION_CODE=2 APP_VERSION_NAME=1.0.1 UPDATE_CHECK_URL="$UPDATE_CHECK_URL" \
  "$GRADLE_HOME/bin/gradle" :app:assembleRelease --no-daemon -Dorg.gradle.jvmargs="-Xmx3g"
cp app/build/outputs/apk/release/app-release.apk dist/landeting-v1.0.1-release.apk

echo ""
echo "✅ 构建完成:"
ls -la dist/*.apk
