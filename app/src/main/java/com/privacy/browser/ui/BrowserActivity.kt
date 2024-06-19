package com.privacy.browser.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.orhanobut.logger.Logger
import com.privacy.browser.R
import com.privacy.browser.databinding.ActivityBrowserBinding
import com.privacy.browser.ui.vm.BrowserVMImpl
import com.privacy.browser.webconfig.MyWebViewClient
import vam.osoles.dores.oll.webconfig.MyWebChromeClient
import vam.osoles.dores.oll.webconfig.WebConfigListener
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/30 10:48
 * version :
 * desc    : 浏览器页面
 **/
class BrowserActivity : AppCompatActivity(), WebConfigListener {
    private lateinit var binding: ActivityBrowserBinding

    private var h5Url: String? = null

    private lateinit var mWebView: WebView

    private val thisVMImpl: BrowserVMImpl
        get() = ViewModelProvider(this)[BrowserVMImpl::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 绑定view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_browser)

        binding.lifecycleOwner = this
        binding.state = this@BrowserActivity
        binding.vm = thisVMImpl

        h5Url = "https://www.baidu.com/"
//        h5Url = "https://mex11apptest.qinjia001.com/ghmas/index.html"
//        h5Url = "https://mbd.baidu.com/newspage/data/landingsuper?context=%7B%22nid%22%3A%22news_9819874017022093980%22%7D"
        initView()
        registerBack()
    }

    private fun initView() {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mWebView = WebView(this)
        mWebView.layoutParams = params
        initWebSetting(mWebView)
    }

    private fun initWebSetting(webView: WebView?) {
        if (TextUtils.isEmpty(h5Url)) return
        val webSettings = webView?.settings
        webSettings?.let {
            it.cacheMode = WebSettings.LOAD_DEFAULT
            it.javaScriptEnabled = true
            it.useWideViewPort = true //将图片调整到适合webview的大小
            it.loadWithOverviewMode = true // 缩放至屏幕的大小
            it.loadsImagesAutomatically = true //支持自动加载图片
            it.defaultTextEncodingName = "utf-8" //设置编码格式
            //适配文件选择
            it.allowContentAccess = true // 是否可访问Content Provider的资源，默认值 true
            it.allowFileAccess = true // 是否可访问本地文件，默认值 true
            it.domStorageEnabled = true // 设置开启web localstorage 缓存（在页面退出后还保存数据）
        }

        webView?.let {
            it.webViewClient = MyWebViewClient(this)
            it.webChromeClient = MyWebChromeClient(this)
            // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
//            it.addJavascriptInterface(new AndroidH5Interface(WebViewActivity.this), "android");
            h5Url?.let {
                webView.loadUrl(h5Url!!)
            }
            binding.flWebContainer.addView(webView)
        }
    }

    /**
     * 刷新
     */
    fun toRefreshPage(){
        val webUrl = thisVMImpl.getWebUrl()
        val clipService = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipService.setPrimaryClip(ClipData.newPlainText("text/plain", webUrl))
        Toast.makeText(this,"复制成功",Toast.LENGTH_SHORT).show()
    }

    fun toTest(){
        mWebView?.let {
            it.loadUrl(thisVMImpl.getWebTitle())
        }
    }

    /**
     * 注册物理回退键 回调
     */
    private fun registerBack() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 如果不处理，则移除当前回调并执行默认的返回行为
                //页面回退
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack()
                } else {
                    remove()
                    finish()
                }
            }
        })
    }

    override fun onProgressChanged(newProgress: Int) {
    }

    override fun onShowFileChooser(
        filePathCallback: ValueCallback<Array<Uri>>,
        accept: String,
        captureEnabled: Boolean
    ) {
        // TODO("Not yet implemented")
    }

    override fun onReceivedError() {
//        TODO("Not yet implemented")
        println("===========================")
    }

    override fun shouldOverrideUrl(url: String?): Boolean {

        Logger.d("shouldOverrideUrl >>> $url")
        try {
            url?.let {
                if (it.startsWith("http:") || it.startsWith("https:")) {
                    return false
                } else {
                    linkParsing(url);
                    return true
                }
            }


        } catch (e: Exception) {
            return true;
        }
        return true
    }



    /**
     * 页面加载完成
     */
    override fun onPageFinished(url: String?) {
        Logger.d("onPageFinished >>> $url")
        thisVMImpl.postWebUrl(url)
        mWebView?.let {
           val  pageTitle = it.title
            thisVMImpl.postWebTitle(pageTitle)
        }
    }


    /**
     * 链接解析
     */
    private fun linkParsing(url: String) {
        // decode编码还原
        try {
            val url = URLDecoder.decode(url, "UTF-8")
            val schemeUrl = getSchemeUrl(url)
            schemeUrl?.let {
                Logger.d("linkParsing >>> $url\n" +
                        "schemeUrl >>> $it")
                mWebView.loadUrl(it)
            }

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    fun isBaiDuScheme(url: String): Boolean {
        var result = false
        url?.let {
            if (url.contains("baiduboxapp://")){
                result = true
            }
        }
        return result
    }

    fun getBDSchemeFun(urlStr: String): String {
        var result = ""
        if (isBaiDuScheme(urlStr)) {
            result = urlStr
            if (urlStr.contains("?")) {
                result = urlStr.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            }
            result = result.replace("baiduboxapp://", "")
        }
        return result
    }

    /**
     * get scheme params
     *
     * @param uri
     * @return
     */
    fun getSchemeParams(url: String): String? {
        // jlan-scheme://getMobeneData?data={}&callback=getMobeneData
        var urlStr = url
        var param:String? = null
        if (isBaiDuScheme(urlStr)) {
            if (urlStr.contains("?")) {
                // hostName ===>  jlan-scheme://getMobeneData
                val hostName = urlStr.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0]
                urlStr = urlStr.replace("$hostName?", "")
                val splitList = urlStr.split("&")

                param = splitList.firstOrNull{
                    it.contains("context={")
                }
            }
        }
        return param
    }

    fun getSchemeUrl(url: String): String? {
        // jlan-scheme://getMobeneData?data={}&callback=getMobeneData
        var urlStr = url
        var param:String? = null
        if (isBaiDuScheme(urlStr)) {

            param = urlStr.substringAfter("&ch_url=").substringBefore("&sourceFrom=")
            param = param?.replace("landingreact","landingshare")
//            if (urlStr.contains("?")) {
//                // hostName ===>  jlan-scheme://getMobeneData
//                val hostName = urlStr.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }
//                    .toTypedArray()[0]
//                urlStr = urlStr.replace("$hostName?", "")
//                val splitList = urlStr.split("&")
//
//                val contextParam = splitList.firstOrNull{
//                    it.contains("context={")
//                }
//                val urlParam = splitList.firstOrNull{
////                    it.contains("context={")
//                    it.contains("ch_url=")
//                }
//                param = urlParam?.let {
//                    val replace = it.replace("ch_url=", "")
//                    val link = contextParam?.let {
//                        val data = it.replace("context=", "")
//                        val dataEncoder = URLEncoder.encode(data,StandardCharsets.UTF_8.toString())
//                        "$replace&context=$dataEncoder"
//                    }
//                    link
//                }
//            }
        }
        return param
    }
}