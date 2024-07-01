package com.privacy.browser.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.privacy.browser.R
import com.privacy.browser.adapter.BindingAdapter
import com.privacy.browser.config.Constants
import com.privacy.browser.databinding.ActivityBrowserHistoryBinding
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.pojo.SearchHistory
import com.privacy.browser.ui.vm.BrowserHistoryVMImpl
import com.wlwork.libframe.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/21 18:09
 * version : 1.0.0
 * desc    :
 **/

@AndroidEntryPoint
class BrowserHistoryActivity: BaseActivity<BrowserHistoryVMImpl,ActivityBrowserHistoryBinding>() {

    private val mAdapter by lazy {
        BindingAdapter<BrowserHistory>(getContext(), R.layout.item_broswer_history)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_browser_history
    }

    override fun initData(savedInstanceState: Bundle?) {
        binding.state = this
        // 注册物理键back事件
        registerBack{
            callResultIntent(onBack = it)
        }

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickListener { _, position ->
            val item = mAdapter.getItem(position) as BrowserHistory
            callResultIntent(linkStr = item.webLink)
        }

        lifecycleScope.launch {
            viewModel.getHistory().flowWithLifecycle(lifecycle).collect{
                Logger.d(it)
                mAdapter.refreshData(it)
            }
        }
    }

    /**
     * 点击退出
     */
    fun onToBack(){
        callResultIntent()
    }

    /**
     * 回调Intent
     */
    private fun callResultIntent(linkStr: String? = null,onBack: OnBackPressedCallback? = null) {
        if (linkStr.isNullOrEmpty()){
            setResult(RESULT_CANCELED)
        }else{
            val intent = Intent()
            intent.putExtra(Constants.DATA,linkStr)
            setResult(RESULT_OK,intent)
        }
        onBack?.remove()
        finish()
    }
}