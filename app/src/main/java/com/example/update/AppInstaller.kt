package com.example.update

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import java.io.File

/**
 * APK 安装器 —— 纯 PackageInstaller 系统安装会话。
 *
 * 关键说明：
 * 1. 应用更新自身（包名相同、签名一致）时，PackageInstaller 会话安装
 *    【不需要】「允许安装未知应用」权限，系统直接原子化替换旧版本，数据平滑保留。
 * 2. 写入 APK 数据后必须先调用 session.fsync(out) 确保数据完整落盘，
 *    否则部分机型会以空错误信息返回 INSTALL_FAILED —— 这是最常见的安装失败原因。
 * 3. 安装结果通过 UpdateInstallReceiver 异步回调（成功 / 失败原因）。
 */
class AppInstaller(private val context: Context) {

    /** 上一次安装提交失败的原因（供 UI 展示真实错误信息） */
    var lastError: String? = null
        private set

    /**
     * 安装新版本 APK（无需任何运行时权限）。
     * @return true = 安装会话已提交，等待系统异步结果（由 UpdateInstallReceiver 回调）；
     *         false = 提交阶段失败，可通过 [lastError] 查看真实原因。
     */
    fun install(apkFile: File): Boolean {
        lastError = null
        if (!apkFile.exists() || apkFile.length() <= 0L) {
            lastError = "APK 文件不存在或为空，请重新下载"
            return false
        }

        var session: PackageInstaller.Session? = null
        return try {
            val packageInstaller = context.packageManager.packageInstaller
            val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            // 关键：声明安装的是本应用自己，系统按「自我更新」处理，无需未知来源权限
            params.setAppPackageName(context.packageName)
            val sessionId = packageInstaller.createSession(params)
            session = packageInstaller.openSession(sessionId)

            // 流式写入 APK 数据；写入完成后必须 fsync，确保数据完整落盘后再 commit
            session.openWrite("landeting_update.apk", 0, apkFile.length()).use { out ->
                apkFile.inputStream().use { input -> input.copyTo(out) }
                session.fsync(out)
            }

            // 提交安装会话，结果通过 UpdateInstallReceiver 广播回调
            val intent = Intent(context, UpdateInstallReceiver::class.java)
            val pending = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            session.commit(pending.intentSender)
            true
        } catch (e: Exception) {
            lastError = e.message?.takeIf { it.isNotBlank() } ?: "创建安装会话失败"
            false
        } finally {
            try {
                session?.close()
            } catch (_: Exception) {
                // 会话关闭失败可忽略
            }
        }
    }

    private companion object {
        const val REQUEST_CODE = 100
    }
}
