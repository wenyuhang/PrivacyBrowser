package com.privacy.browser.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.orhanobut.logger.Logger
import com.privacy.browser.R
import com.privacy.browser.config.Constants
import com.privacy.browser.databinding.ActivityBrowserBinding
import com.privacy.browser.ui.vm.BrowserVMImpl
import com.privacy.browser.webconfig.MyWebViewClient
import com.privacy.browser.webconfig.MyWebChromeClient
import com.privacy.browser.webconfig.WebConfigListener
import com.privacy.browser.webconfig.WebViewUtils
import com.wlwork.libframe.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import javax.inject.Inject

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/30 10:48
 * version :
 * desc    : 浏览器页面
 **/

@AndroidEntryPoint
class BrowserActivity : BaseActivity<BrowserVMImpl, ActivityBrowserBinding>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_browser
    }

    private var webView: WebView? = null

    @Inject
    lateinit var webViewUtils: WebViewUtils

    override fun initData(savedInstanceState: Bundle?) {

//        h5Url = "https://www.baidu.com/"
//        h5Url = "https://mex11apptest.qinjia001.com/ghmas/index.html"
//        h5Url = "https://mbd.baidu.com/newspage/data/landingsuper?context=%7B%22nid%22%3A%22news_9819874017022093980%22%7D"

        initObserve()

        binding.state = this
        binding.vm = viewModel
//        binding.lifecycleOwner = this
        binding.executePendingBindings()

        // 初始化webview
        initWebView()

        // 跳转至搜索页面
        binding.btnShowWebUrl.setOnClickListener {
            resultLauncher?.launch(viewModel.getBuildIntent(this))
        }
        // 点击时间
        binding.btnSearchEngine.setOnClickListener {
            resultLauncher?.launch(Intent(this, BrowserHistoryActivity::class.java))
        }
    }

    private fun initObserve() {
    }


    private fun initWebView() {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        webView = WebView(this)
        webView?.also {
            it.layoutParams = params
            webViewUtils.initWebSetting(
                WebSettings.LOAD_DEFAULT,
                it,
                viewModel.baseEngineUrl,
                MyWebViewClient(webListener),
                MyWebChromeClient(webListener)
            )
            binding.flWebContainer.addView(it)
        }

        // 注册物理键back事件
        registerBack {
            // 如果不处理，则移除当前回调并执行默认的返回行为
            //页面回退
            if (webView != null && webView?.canGoBack() == true) {
                webView?.goBack()
            } else {
                it.remove()
                finish()
            }
        }

        // 注册跳转回调 StartActivityForResult()
        registerLauncher {
            if (it.resultCode == RESULT_OK) {
                // 提取url
                val linkStr = it.data?.getStringExtra(Constants.DATA)
                pageLoadUrl(linkStr)
            }
        }
    }


    /**
     * 刷新
     */
    fun toRefreshPage() {
        webView?.apply {
            settings?.cacheMode = WebSettings.LOAD_NO_CACHE
            reload()
        }
//        pageLoadUrl(viewModel.getWebUrl())
    }


    private val webListener = object : WebConfigListener {
        override fun onProgressChanged(newProgress: Int) {
//            TODO("Not yet implemented")
        }

        override fun onShowFileChooser(
            filePathCallback: ValueCallback<Array<Uri>>,
            accept: String,
            captureEnabled: Boolean
        ) {
//            TODO("Not yet implemented")
        }

        override fun onReceivedError() {
//            TODO("Not yet implemented")
        }

        override fun shouldOverrideUrl(url: String): Boolean {
            return if (url.startsWith("http:") || url.startsWith("https:")) {
                viewModel.postWebTitle(url)
                false
            } else {
                linkParsing(url);
                true
            }
            return false
        }

        /**
         * 页面加载完成
         */
        override fun onPageFinished(url: String?) {
            viewModel.postWebUrl(url)
            webView?.let {
                val pageTitle = it.title
                Logger.d("onPageFinished >>> $url \n $pageTitle")
                viewModel.postWebTitle(pageTitle)
                // 插入数据库表
                viewModel.insertSerarchHistory(title = pageTitle, link = url)
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
//                    Logger.d("linkParsing >>> $url\n" +
//                            "schemeUrl >>> $it")
                pageLoadUrl(schemeUrl)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }

        fun isBaiDuScheme(url: String): Boolean {
            var result = false
            url?.let {
                if (url.contains("baiduboxapp://")) {
                    result = true
                }
            }
            return result
        }

        fun getSchemeUrl(url: String): String? {
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

    private fun pageLoadUrl(url: String?) {
        url?.let {
            if (it.startsWith("http:") || it.startsWith("https:")) {
                webView?.settings?.cacheMode = WebSettings.LOAD_DEFAULT
                webView?.loadUrl(it)
                viewModel.postWebTitle(it)
            }
        }
    }

//    fun getSchemeParams(url: String): String? {
//        // jlan-scheme://getMobeneData?data={}&callback=getMobeneData
//        var urlStr = url
//        var param:String? = null
//        if (isBaiDuScheme(urlStr)) {
//            if (urlStr.contains("?")) {
//                // hostName ===>  jlan-scheme://getMobeneData
//                val hostName = urlStr.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }
//                    .toTypedArray()[0]
//                urlStr = urlStr.replace("$hostName?", "")
//                val splitList = urlStr.split("&")
//
//                param = splitList.firstOrNull{
//                    it.contains("context={")
//                }
//            }
//        }
//        return param
//    }

}