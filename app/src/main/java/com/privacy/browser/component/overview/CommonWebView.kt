package com.privacy.browser.component.overview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.UnsupportedEncodingException
import java.net.URLDecoder


@SuppressLint("SetJavaScriptEnabled", "ViewConstructor")
class CommonWebView(context: Context) : WebView(context) {
    //    var isInRecent = false
    init {
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.databaseEnabled = true

        webChromeClient = object : WebChromeClient() {
            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                icon?.let {
                    val bd = BitmapDrawable(context.resources, it)
                    WebTabManager.getInstance().updateLastTabIcon(bd)
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                title?.let {
                    WebTabManager.getInstance().updateLastTabTitle(it)
                }

            }

            /**
             * 页面加载进度
             */
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                onProgressChangedFun?.let {
                    it(newProgress)
                }
            }
        }

        webViewClient = object : WebViewClient() {

            /**
             * 页面加载完成
             */
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // 关闭百度打开页面
                closeBaiDuOpenModule(view)

                // 添加执行回调
                onPageFinishedFun?.let {
                    it(url.orEmpty())
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return try {
                    val url = request?.url.toString()
                    shouldOverrideUrl(url)
                } catch (e: Exception) {
                    super.shouldOverrideUrlLoading(view, request)
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return try {
                    shouldOverrideUrl(url)
                } catch (e: Exception) {
                    super.shouldOverrideUrlLoading(view, url)
                }
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
//                val url = request?.url.toString()
//                if (url.endsWith(".gif")) {
//                    return WebResourceResponse("text/html", "utf-8", null)
//                }
                return super.shouldInterceptRequest(view, request)

            }
        }

        loadUrl("https:www.baidu.com")
    }

    /**
     * 页面加载完成
     */
    private var onPageFinishedFun: ((String?) -> Unit)? = null
    fun addOnPageFinished(operation: (String?) -> Unit) {
        this.onPageFinishedFun = operation
    }

    /**
     * 页面加载的URL
     */
    private var shouldOverrideUrlFun: ((String) -> Unit)? = null
    fun addShouldOverrideUrl(operation: (String) -> Unit) {
        this.shouldOverrideUrlFun = operation
    }


    /**
     * 页面加载进度
     */
    private var onProgressChangedFun: ((Int) -> Unit)? = null
    fun addOnProgressChanged(operation: (Int) -> Unit) {
        this.onProgressChangedFun = operation
    }

    /**
     * 加载的url
     */
    fun shouldOverrideUrl(url: String?): Boolean {
        url?.let {
            return if (it.startsWith("http:") || it.startsWith("https:")) {
//                viewModel.postWebTitle(it)
                // 添加执行回调
                shouldOverrideUrlFun?.let {
                    it(url)
                }
                false
            } else {
                linkParsing(it);
                true
            }
        }

        return false
    }

    /**
     * 加载url
     */
    fun pageLoadUrl(url: String?) {
        url?.let {
            if (it.startsWith("http:") || it.startsWith("https:")) {
                settings?.cacheMode = WebSettings.LOAD_DEFAULT
                loadUrl(it)
//                viewModel.postWebTitle(it)
            }
        }
    }

    /**
     * 关闭百度打开页面
     */
    private fun closeBaiDuOpenModule(webView: WebView?) {
        try {
            val jsCode = "document.getElementById('mainContentContainer').style.height='auto';" +
                    "document.getElementById('mainContentContainer').style.overflow='auto';" +
                    "document.getElementById('bdrainrwDragButton').style.display='none';" +
                    "document.getElementById('headDeflectorContainer').style.display='none';" +
                    "var elements = document.getElementsByClassName('foldMaskWrapper');" +
                    "for (var i = 0; i < elements.length; i++) {" +
                    "   elements[i].style.display = 'none';" +
                    "}";
            webView?.evaluateJavascript(jsCode, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /*************************** 解析url ***************************/

    /**
     * 链接解析
     */
    private fun linkParsing(url: String) {
        // decode编码还原
        try {
            val url = URLDecoder.decode(url, "UTF-8")
            val schemeUrl = getSchemeUrl(url)
//                    Logger.d("linkParsing >>> $url\n" +
//                            "schemeUrl >>> $it")
            pageLoadUrl(schemeUrl)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    private fun isBaiDuScheme(url: String): Boolean {
        var result = false
        url?.let {
            if (url.contains("baiduboxapp://")) {
                result = true
            }
        }
        return result
    }

    private fun getSchemeUrl(url: String): String? {
        // jlan-scheme://getMobeneData?data={}&callback=getMobeneData
        var urlStr = url
        var param: String? = null
        if (isBaiDuScheme(urlStr)) {

            param = urlStr.substringAfter("&ch_url=").substringBefore("&sourceFrom=")
            param = param?.replace("landingreact", "landingshare")
        }
        return param
    }

}