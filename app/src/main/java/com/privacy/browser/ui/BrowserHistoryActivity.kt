package com.privacy.browser.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
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
import java.nio.charset.StandardCharsets

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

        mAdapter.apply {
            setOnItemClickListener { _, position ->
                val item = mAdapter.getItem(position) as BrowserHistory
                callResultIntent(linkStr = item.webLink)
            }
            setOnItemLongClickListener { browserHistory, _ ->
                val clipService = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                clipService.setPrimaryClip(ClipData.newPlainText("text/plain", browserHistory.webLink))
                showToast("复制成功")
            }
            setChildIdList(listOf(R.id.btn_delete))
            setOnItemChildClickListener { v, position ->
                if (v.id == R.id.btn_delete){
                    val item = mAdapter.getItem(position) as BrowserHistory
                    Logger.e("v 点击删除了 $position  id: ${item.id}")
                    viewModel.deleteHistoryById(item.id)
                }
            }
        }


        binding.layoutRefresh.apply {
            setEnableLoadMore(false)
            setOnRefreshListener {
                requestHistoryData(1)
            }
            setOnLoadMoreListener {
                requestHistoryData(curPage)
            }
        }


        viewModel.browserHistoryLiveData.observe(this){
            this.curPage = it.curPage
            updateHistoryRecyUI(it.listData, curPage < 2,it.isLoadMore)
        }
        requestHistoryData(curPage)

        // 此方法是执行批量数据插入的  慎用!!!
//        viewModel.executeSql(readSqlFile(this, "browser_history.sql").split(";"))
    }

    private fun readSqlFile(context: Context, fileName: String): String {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer, StandardCharsets.UTF_8)
    }

    /**
     * 更新recycler
     */
    private fun updateHistoryRecyUI(data: List<BrowserHistory>?, isRefresh: Boolean, isCanLoadMore: Boolean) {
        data?.let {
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
        binding.layoutRefresh.closeHeaderOrFooter()
    }
}