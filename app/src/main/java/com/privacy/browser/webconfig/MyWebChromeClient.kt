package com.privacy.browser.webconfig

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/8 16:14
 * version : 1.0.0
 * desc    :
 */
class MyWebChromeClient(private val webConfigListener: WebConfigListener) : WebChromeClient() {
    override fun onProgressChanged(view: WebView, newProgress: Int) {
        webConfigListener.onProgressChanged(newProgress)
        super.onProgressChanged(view, newProgress)
    }

    override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
    ): Boolean {
        val acceptTypes = fileChooserParams.acceptTypes
        // 是否为直接拍照模式
        val captureEnabled = fileChooserParams.isCaptureEnabled
        return if (acceptTypes != null && acceptTypes.isNotEmpty()) {
            webConfigListener.onShowFileChooser(filePathCallback,acceptTypes[0],captureEnabled)
            true
        } else {
            false
        }
    }
}