package com.privacy.browser.ui

import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.orhanobut.logger.Logger
import com.privacy.browser.R
import com.privacy.browser.component.overview.CommonWebView
import com.privacy.browser.component.overview.WebTabManager
import com.privacy.browser.config.Constants
import com.privacy.browser.databinding.ActivityWebBinding
import com.privacy.browser.ui.vm.WebVMImpl
import com.wlwork.libframe.base.BaseActivity
import com.wlwork.libframe.utils.LiveDataBus
import dagger.hilt.android.AndroidEntryPoint

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/26 16:03
 * version :
 * desc    :
 **/

@AndroidEntryPoint
class WebActivity : BaseActivity<WebVMImpl, ActivityWebBinding>() {

    private lateinit var mWebView: CommonWebView

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initData(savedInstanceState: Bundle?) {
        binding.state = this
        binding.vm = viewModel

        // 配置webview
        initWebView()

        // 注册物理键back事件
        registerBack {
            // 如果不处理，则移除当前回调并执行默认的返回行为
            //页面回退
            if (mWebView != null && mWebView?.canGoBack() == true) {
                mWebView?.goBack()
            } else {
                it.remove()
                toAddNewWindow()
            }
        }

        // 注册跳转回调 StartActivityForResult()
        registerLauncher {
            if (it.resultCode == RESULT_OK) {
                // 提取url
                val linkStr = it.data?.getStringExtra(Constants.DATA)
                mWebView.pageLoadUrl(linkStr)
                viewModel.postWebTitle(linkStr)
            }
        }

        /** 注册点击事件 **/
        // 跳转至搜索页面
        binding.btnShowWebUrl.setOnClickListener {
            resultLauncher?.launch(viewModel.getBuildIntent(this))

        }
        // 跳转至历史浏览页面
        binding.btnSearchEngine.setOnClickListener {
            resultLauncher?.launch(Intent(this, BrowserHistoryActivity::class.java))
        }
    }

    /**
     * 配置webview
     */
    private fun initWebView() {
        val wb = WebTabManager.getInstance().getCacheWebTab().last()
        mWebView = wb.webView
        mWebView?.apply {
            addOnPageFinished {
                viewModel.postWebUrl(url)
                mWebView?.apply {
                    val pageTitle = title
                    Logger.d("onPageFinished >>> $url \n $pageTitle")
                    viewModel.postWebTitle(pageTitle)
                    // 插入数据库表
                    viewModel.insertSerarchHistory(title = pageTitle, link = url)
                }
            }

            addShouldOverrideUrl {
                viewModel.postWebTitle(it)
            }

            var pageTitle = title
            if(pageTitle.isNullOrEmpty()){
                pageTitle = url
            }
            viewModel.postWebTitle(pageTitle)
        }

        binding.flWebContainer.addView(mWebView)
    }

    override fun onResume() {
        super.onResume()
        mWebView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mWebView.onPause()
    }


    override fun finish() {
        viewModel.catchWebView(this)
        super.finish()
    }

    /**
     * 刷新
     */
    fun toRefreshPage() {
        mWebView?.apply {
            settings?.cacheMode = WebSettings.LOAD_NO_CACHE
            reload()
        }
    }

    /**
     * 添加新窗口
     */
    fun toAddNewWindow() {
        WebTabManager.getInstance().divideAllWebView()
        finish()
        LiveDataBus.get().with("key_test").value = "addNewWindow"
    }

}