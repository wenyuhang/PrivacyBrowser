package com.privacy.browser.webconfig

import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.orhanobut.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/3/7 14:33
 * version : 1.0.0
 * desc    :
 */

class WebViewUtils{
    fun initWebSetting(
        cacheMode:Int,
        mWebView: WebView?,
        h5Url: String?,
        webViewClient: WebViewClient?,
        webChromeClient: WebChromeClient?
    ) {
        if (TextUtils.isEmpty(h5Url) || mWebView == null) return
//        com.orhanobut.logger.Logger.d("===>$cacheMode")
        val webSettings = mWebView.settings
        webSettings.cacheMode = cacheMode
//        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式
        //适配文件选择
        webSettings.allowContentAccess = true // 是否可访问Content Provider的资源，默认值 true
        webSettings.allowFileAccess = true // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSettings.allowFileAccessFromFileURLs = false
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSettings.allowUniversalAccessFromFileURLs = false
        // 设置开启web localstorage 缓存（在页面退出后还保存数据）
        webSettings.domStorageEnabled = true
        mWebView.webViewClient = webViewClient!!
        mWebView.webChromeClient = webChromeClient
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        mWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        mWebView.loadUrl(h5Url!!)
    }

    /**
     * 原生执行调用Js函数
     */
    fun invokJs(mWebView: WebView?, funName: String?, data: String?) {
        if (TextUtils.isEmpty(funName)) return
        if (null == mWebView) return
        var jsFun: String? = null
        jsFun = if (TextUtils.isEmpty(data)) {
            "javascript:$funName()"
        } else {
            "javascript:$funName('$data')"
        }
        Logger.e("TAG-WEB invokJS: $jsFun");
        mWebView.evaluateJavascript(jsFun){ }
    }

//    单例实现
//    companion object {
//        @Volatile
//        private var webViewUtils: WebViewUtils? = null
//        fun getInstance(): WebViewUtils {
//            return webViewUtils ?: synchronized(this) {
//                webViewUtils ?: WebViewUtils().also { webViewUtils = it }
//            }
//        }
//    }
}