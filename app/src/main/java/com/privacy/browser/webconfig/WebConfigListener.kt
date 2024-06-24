package com.privacy.browser.webconfig

import android.net.Uri
import android.webkit.ValueCallback

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/8 16:17
 * version : 1.0.0
 * desc    :
 */
interface WebConfigListener {
    // webchromeclient //
    fun onProgressChanged(newProgress: Int)
    fun onShowFileChooser(filePathCallback: ValueCallback<Array<Uri>>,accept: String,captureEnabled: Boolean)

    // webviewclient //
    fun onReceivedError()
    fun shouldOverrideUrl(url: String): Boolean
    fun onPageFinished(url: String?)
}