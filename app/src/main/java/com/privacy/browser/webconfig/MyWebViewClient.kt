package com.privacy.browser.webconfig

import android.net.http.SslError
import android.os.Build
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.orhanobut.logger.Logger

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/10 14:31
 * version : 1.0.0
 * desc    :
 */
class MyWebViewClient(private val webConfigListener: WebConfigListener) : WebViewClient() {
    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        handler.cancel()
    }

    /**
     * 加载失败
     * @param view The WebView that is initiating the callback.
     * @param request The originating request.
     * @param error Information about the error occurred.
     */
    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        /**  错误日志打印   */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            println("Error===>${request.url}==${error.errorCode}==${error.description}");
        }
        webConfigListener.onReceivedError()
        super.onReceivedError(view, request, error)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return try {
            val url = request.url.toString()
            webConfigListener.shouldOverrideUrl(url)
        }catch (e: Exception){
            super.shouldOverrideUrlLoading(view, request)
        }

    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return try {
            webConfigListener.shouldOverrideUrl(url)
        }catch (e: Exception){
            super.shouldOverrideUrlLoading(view,url)
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        // 关闭百度打开页面
        closeBaiDuOpenModule(view)

        webConfigListener.onPageFinished(url)
    }

    /**
     * 关闭百度打开页面
     */
    private fun closeBaiDuOpenModule(webView: WebView) {
        try {
            val jsCode = "document.getElementById('mainContentContainer').style.height='auto';" +
                    "document.getElementById('mainContentContainer').style.overflow='auto';"+
                    "document.getElementById('bdrainrwDragButton').style.display='none';"+
                    "document.getElementById('headDeflectorContainer').style.display='none';"+
                    "var elements = document.getElementsByClassName('foldMaskWrapper');" +
                    "for (var i = 0; i < elements.length; i++) {" +
                    "   elements[i].style.display = 'none';" +
                    "}";
            webView.evaluateJavascript(jsCode, null)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}