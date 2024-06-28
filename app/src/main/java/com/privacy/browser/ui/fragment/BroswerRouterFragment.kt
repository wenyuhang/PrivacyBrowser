package com.privacy.browser.ui.fragment

import android.content.Intent
import android.os.Bundle
import com.privacy.browser.R
import com.privacy.browser.databinding.FragmentBroswerRouterBinding
import com.privacy.browser.ui.BrowserRouterActivity
import com.wlwork.libframe.base.BaseFragment
import com.wlwork.libframe.base.BaseViewModel

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/28 15:01
 * version : 1.0.0
 * desc    :
 **/

class BroswerRouterFragment : BaseFragment<BaseViewModel, FragmentBroswerRouterBinding>(){
    override fun getLayoutId(): Int {
        return R.layout.fragment_broswer_router
    }

    override fun initData(savedInstanceState: Bundle?) {
        binding.state = this
    }

    fun jumpWebPage(){
        startActivity(Intent(activity,BrowserRouterActivity::class.java))
    }
}