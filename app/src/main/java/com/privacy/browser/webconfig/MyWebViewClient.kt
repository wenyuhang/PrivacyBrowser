package vam.osoles.dores.oll.webconfig

import android.net.http.SslError
import android.os.Build
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi

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
//        println("==========================="+error.errorCode+error.description)
        webConfigListener.onReceivedError()
        super.onReceivedError(view, request, error)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        return webConfigListener.shouldOverrideUrl(url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return webConfigListener.shouldOverrideUrl(url)
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        webConfigListener.onPageFinished(url)
    }
}