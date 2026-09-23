# 懒得听 (Landeting Music)

全中文高品质音乐播放器：本地音乐扫描、网络音乐搜索、HIFI 专区、动态卡通更新弹窗、APK 自动下载升级。

## ✨ 功能总览

| 功能 | 说明 |
| --- | --- |
| 🎵 本地音乐扫描 | 自动扫描设备本地音乐文件，识别歌手/专辑/时长 |
| 🌐 网络音乐搜索 | 调用网易云公开搜索接口，实时检索全网曲库 |
| 🔥 HIFI 专区 | 高保真音质专区，发烧友优选 |
| 🎛️ 均衡器 | 多预设 EQ、Bass Boost、3D 环绕 |
| 🎤 听歌识曲 | 模拟声学指纹识别匹配歌曲 |
| 😴 睡眠定时 | 定时暂停播放，陪你入睡 |
| 🛡️ 青少年模式 | 一键开启纯净模式 |
| 🚀 自动更新 | 检测新版本 → 动态卡通弹窗 → 自动下载 APK → 自动安装升级 |

## 🔄 自动更新机制（核心）

### 工作流程

```
App 启动
   │
   ├─ AppUpdateChecker.fetchLatestInfo()
   │     ├─ 拉取远端 update.json (BuildConfig.UPDATE_CHECK_URL)
   │     └─ 失败时回退内置 assets/update_demo.json (演示模式)
   │
   ├─ hasNewVersion(): 远端 versionCode > 本地 versionCode ?
   │
   ├─ YES → CartoonUpdateDialog 动态卡通弹窗（Canvas 全手绘音乐猫）
   │        ├─ 检测中 (Checking)         - 猫咪待机动画
   │        ├─ 发现新版本 (Found)        - 版本对照条 + 更新说明 + 「立即更新」
   │        ├─ 下载中 (Downloading)      - 进度环 + 均衡器动效
   │        ├─ 安装中 (Installing)       - 火箭发射动画
   │        ├─ 完成 (Done)               - 彩带庆祝动画
   │        └─ 错误/权限 (Error/NeedInstallPermission)
   │
   ├─ ApkDownloader.download() → 流式下载到应用专属目录 (无需存储权限)
   │
   └─ AppInstaller.install()
         ├─ PackageInstaller 系统安装会话（原子化替换旧版本）
         └─ 兜底 FileProvider + ACTION_VIEW 系统安装器
```

### 关键说明

- **「卸载旧版本 + 安装新版本」**：Android 上安装与本地**签名一致、包名相同**的 APK，系统即视为"卸载旧版并安装新版"，数据与账号平滑保留。
- **签名要求**：旧版（1.0.0）与新版本（1.0.1）必须使用**同一把签名密钥**（`my-upload-key.jks`），否则无法覆盖安装。
- **权限**：Android 8.0+ 需用户在系统弹窗中允许「安装未知应用」，应用会引导跳转设置页。
- **强制更新**：`update.json` 中 `forceUpdate: true` 时，弹窗不可关闭，必须更新才能继续使用。

### 更新清单 update.json 字段

```json
{
  "versionCode": 2,            // 版本号（比较大小判定是否有新版本）
  "versionName": "1.0.1",      // 版本名（展示用）
  "downloadUrl": "https://...apk",  // APK 下载地址
  "apkSize": 20609886,         // APK 大小（字节）
  "forceUpdate": false,        // 是否强制更新
  "md5": "",                   // APK MD5 校验（可选）
  "releaseNotes": ["更新说明1", "更新说明2"]
}
```

## 📦 构建与发布

### 环境要求

- JDK 21
- Android SDK (platform 36.1, build-tools 36.0.0)
- Gradle 9.3.1

### 一键构建两个版本

```bash
./scripts/build_apks.sh
# 产物:
#   dist/landeting-v1.0.0-release.apk  (versionCode=1, 旧版)
#   dist/landeting-v1.0.1-release.apk  (versionCode=2, 新版)
```

构建脚本会自动内置远端更新检查地址，旧版启动后即可检测到新版并触发更新。

### 一键发布新版本

```bash
export GH_TOKEN=你的_github_token
./scripts/publish_update.sh dist/landeting-v1.0.2.apk 3 1.0.2 "新增功能" "修复问题"
```

脚本自动完成：
1. 校验 APK 元数据
2. 创建/更新 GitHub Release 并上传 APK
3. 生成 `update.json` 更新清单（versionCode、下载地址、大小、更新说明）
4. 推送 `update.json` 到仓库 main 分支

之后所有已安装的旧版本手机在启动后（或设置页手动检查）即可收到更新通知。

### 手动更新检查

设置页 → 「检查更新」→ 立即触发一次检测。

## 🛠️ 技术栈

- Kotlin + Jetpack Compose (Material 3)
- MVVM (ViewModel + StateFlow)
- Room (均衡器配置持久化)
- OkHttp / Retrofit / Moshi (网络)
- Coil (图片加载)
- 动态卡通弹窗：Compose Canvas 全手绘 + 无限动画

## 📁 项目结构

```
app/src/main/java/com/example/
├── MainActivity.kt            # 主界面 + 更新弹窗挂载
├── data/                      # 数据层
│   ├── LocalAudioScanner.kt   # 本地音乐扫描
│   ├── NetworkMusicSearcher.kt # 网络音乐搜索
│   ├── MusicRepository.kt     # 内置曲库
│   └── local/                 # Room 数据库
├── model/                     # 数据模型
├── player/AudioEngine.kt      # 音频引擎
├── ui/
│   ├── components/
│   │   └── CartoonUpdateDialog.kt  # 🎨 动态卡通更新弹窗
│   ├── screens/               # 首页/HIFI/我的
│   └── theme/                 # 主题配色
├── update/                    # 🚀 自动更新模块
│   ├── AppUpdateChecker.kt    # 版本检测
│   ├── ApkDownloader.kt       # APK 下载
│   ├── AppInstaller.kt        # APK 安装
│   ├── UpdateInstallReceiver.kt # 安装结果接收
│   └── UpdateModels.kt        # 更新状态机
└── viewmodel/MusicPlayerViewModel.kt # 更新流程编排
```

## 🔗 在线资源

- 更新清单: https://raw.githubusercontent.com/shuting52/landeting/main/update.json
- v1.0.0 下载: https://github.com/shuting52/landeting/releases/download/v1.0.0/landeting-v1.0.0-release.apk
- v1.0.1 下载: https://github.com/shuting52/landeting/releases/download/v1.0.1/landeting-v1.0.1-release.apk

## 📜 更新日志

- **v1.0.1** (2026)
  - 全新动态卡通更新弹窗
  - 自动检测新版本，一键下载安装最新 APK
  - 新增网络音乐搜索，海量曲库实时检索
  - 优化本地音乐扫描，更快更准确
  - 修复若干已知问题，播放更稳定
- **v1.0.0** 首发版
  - 本地音乐扫描
  - 开屏动画 / 听歌识曲 / 睡眠定时 / HIFI 专区
