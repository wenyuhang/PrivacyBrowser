package com.privacy.browser.ui.fragment

import android.os.Bundle
import com.privacy.browser.R
import com.privacy.browser.databinding.FragmentHomeBinding
import com.privacy.browser.databinding.FragmentMineBinding
import com.wlwork.libframe.base.BaseFragment
import com.wlwork.libframe.base.BaseViewModel

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/28 15:01
 * version : 1.0.0
 * desc    :
 **/

class MineFragment : BaseFragment<BaseViewModel,FragmentMineBinding>(){
    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}