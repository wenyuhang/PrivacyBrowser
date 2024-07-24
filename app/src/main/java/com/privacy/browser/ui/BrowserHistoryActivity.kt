package com.privacy.browser.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import com.privacy.browser.R
import com.privacy.browser.ui.adapter.BindingAdapter
import com.privacy.browser.config.Constants
import com.privacy.browser.databinding.ActivityBrowserHistoryBinding
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.ui.vm.BrowserHistoryVMImpl
import com.wlwork.libframe.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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

    // page
    private var curPage = 1

    override fun initData(savedInstanceState: Bundle?) {
        binding.state = this
        // 注册物理键back事件
        registerBack{
            callResultIntent(onBack = it)
        }

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            isNestedScrollingEnabled = false
        }

        mAdapter.setOnItemClickListener { _, position ->
            val item = mAdapter.getItem(position) as BrowserHistory
            callResultIntent(linkStr = item.webLink)
        }

        binding.layoutRefresh.apply {
            setEnableLoadMore(false)
            setOnRefreshListener {
                Logger.e("刷新了")
                requestHistoryData(1)
            }
            setOnLoadMoreListener {
                Logger.e("加载了")
                requestHistoryData(curPage)
            }
        }


        viewModel.browserHistoryLiveData.observe(this){
            Logger.e("===>执行了")
            this.curPage = it.curPage
            updateHistoryRecyUI(it.listData, curPage < 2,it.isLoadMore)
        }
        requestHistoryData(curPage)
    }

    /**
     * 更新recycler
     */
    private fun updateHistoryRecyUI(data: List<BrowserHistory>, isRefresh: Boolean, isCanLoadMore: Boolean) {
        data.let {
            // 是否为刷新数据
            if (isRefresh) mAdapter.refreshData(it) else mAdapter.addData(it)
            // 是否可以加载更多
            if (isCanLoadMore){
                binding.layoutRefresh.setEnableLoadMore(true)
                curPage++
            }else {
                binding.layoutRefresh.setEnableLoadMore(false)
                binding.layoutRefresh.finishLoadMoreWithNoMoreData()
            }
            binding.layoutRefresh.closeHeaderOrFooter()
        }



        // 是否加载空布局
        if (mAdapter.itemCount == 0){
//            if (mAdapter.emptyLayout == null) {
//                mAdapter.setEmptyView(R.layout.layout_empty)
//            }
        }
    }

    /**
     * 请求浏览器历史数据
     */
    private fun requestHistoryData(curPage: Int){
        this.curPage = curPage
        viewModel.getRequestData(curPage, Constants.PAGE_SIZE)
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

    override fun hideLoading() {
        super.hideLoading()
        Logger.e("执行了")
        binding.layoutRefresh.closeHeaderOrFooter()
    }
}