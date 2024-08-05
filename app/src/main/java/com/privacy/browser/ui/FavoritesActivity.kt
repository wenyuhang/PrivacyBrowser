package com.privacy.browser.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.privacy.browser.R
import com.privacy.browser.config.Constants
import com.privacy.browser.databinding.ActivityFavoritesBinding
import com.privacy.browser.pojo.Favorites
import com.privacy.browser.ui.adapter.BindingAdapter
import com.privacy.browser.ui.vm.FavoritesVMImpl
import com.wlwork.libframe.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/7/26 17:25
 * version : 1.0.0
 * desc    : 收藏页
 **/

@AndroidEntryPoint
class FavoritesActivity : BaseActivity<FavoritesVMImpl,ActivityFavoritesBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_favorites
    }

    // page
    private var curPage = 1
    private val mAdapter by lazy {
        BindingAdapter<Favorites>(getContext(), R.layout.item_favorites)
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
            isNestedScrollingEnabled = false
        }

        mAdapter.setOnItemClickListener { _, position ->
            val item = mAdapter.getItem(position) as Favorites
            callResultIntent(linkStr = item.webLink)
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


        viewModel.favoritesLiveData.observe(this){
            this.curPage = it.curPage
            updateHistoryRecyUI(it.listData, curPage < 2,it.isLoadMore)
        }

        requestHistoryData(curPage)

        mAdapter.setOnItemLongClickListener { browserHistory, _ ->
            val clipService = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipService.setPrimaryClip(ClipData.newPlainText("text/plain", browserHistory.webLink))
            showToast("复制成功")
        }
    }


    /**
     * 更新recycler
     */
    private fun updateHistoryRecyUI(data: List<Favorites>?, isRefresh: Boolean, isCanLoadMore: Boolean) {
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

}