package com.privacy.browser

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.privacy.browser.databinding.ActivityBrowserBinding
import vam.osoles.dores.oll.webconfig.MyWebChromeClient
import vam.osoles.dores.oll.webconfig.MyWebViewClient
import vam.osoles.dores.oll.webconfig.WebConfigListener

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/30 10:48
 * version :
 * desc    : 浏览器页面
 **/

class BrowserActivity : AppCompatActivity() , WebConfigListener {
    private lateinit var binding: ActivityBrowserBinding

    private var h5Url:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        h5Url = "https://www.baidu.com/"
        initView()
    }

    private fun initView() {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val mWebView = WebView(this)
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
                webView.loadUrl(it)
            }
        }

        binding.root.addView(webView)
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
    }

    override fun shouldOverrideUrl(url: String?): Boolean {
//        try{
//            url?.let {
//                if (it.startsWith("http:") || it.startsWith("https:")) {
//                    return false
//                }
//            }
//
//
//            if(url.startsWith("http")||url.startsWith("https")){
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//                return true;
//            }
//        }catch (e: Exception){
//            return true;
//        }
//        webView.loadUrl(url);
        return true;
    }

    override fun onPageFinished(url: String?) {
//        TODO("Not yet implemented")
    }
}