package com.privacy.browser.ui

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.base.adapter.divider.DividerItemDecoration
import com.privacy.browser.R
import com.privacy.browser.adapter.BindingAdapter
import com.privacy.browser.databinding.ActivityWebSearchBinding
import com.privacy.browser.pojo.SearchHistoryBean
import com.privacy.browser.ui.vm.WebSearchVMImpl
import com.wlwork.libframe.base.BaseActivity
import com.wlwork.libframe.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 9:37
 * version : 1.0.0
 * desc    : 搜索页面
 **/

@AndroidEntryPoint
class WebSearchActivity: BaseActivity<WebSearchVMImpl, ActivityWebSearchBinding>() {

    private val mAdapter by lazy {
        BindingAdapter<SearchHistoryBean>(getContext(),R.layout.item_search_history)
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_web_search
    }

    override fun initData(savedInstanceState: Bundle?) {
        with(binding.recyclerView){
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL,
                    R.drawable.list_divider_8
                )
            )
            adapter = mAdapter
        }
        viewModel.getOilPriceInfo()
    }


}