package com.privacy.browser.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.privacy.browser.R
import com.privacy.browser.databinding.ActivityMainBinding
import com.privacy.browser.ui.fragment.BroswerRouterFragment
import com.privacy.browser.ui.fragment.HomeFragment
import com.privacy.browser.ui.fragment.MineFragment
import com.wlwork.libframe.base.BaseActivity
import com.wlwork.libframe.base.BaseFragment
import com.wlwork.libframe.base.BaseViewModel

class MainActivity : BaseActivity<BaseViewModel,ActivityMainBinding>() {

    private val TAB_HOME = 0
    private val TAB_BROSWER = 1
    private val TAB_MINE = 3

    private var homeFragment: HomeFragment? = null
    private var broswerRouterFragment: BroswerRouterFragment? = null
    private var mineFragment: MineFragment? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        // tab注册点击事件
        registerTabOnClick()
        // 初始化首页内容
        selectFragment(TAB_HOME)
    }

    private fun registerTabOnClick() {
        // 首页
        binding.btnTabHome.setOnClickListener{
            selectFragment(TAB_HOME)
        }
        // 浏览器
        binding.btnTabNews.setOnClickListener {
            selectFragment(TAB_BROSWER)
        }
        // 我的
        binding.btnTabMine.setOnClickListener {
            selectFragment(TAB_MINE)
        }
    }

    /**
     * 选择要展示的fragment
     *
     * @param type
     */
    private fun selectFragment(type: Int) {
        // 在fragment切换的时候 记录当前页面索引
//        viewModel.setPageNum(type)
        // fragment切换
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        hideFragment(transaction)
        when (type) {
            TAB_HOME -> if (homeFragment == null) {
                homeFragment = HomeFragment()
                transaction.add(R.id.frame_container, homeFragment!!)
            } else {
                showFragment(transaction, homeFragment)
            }

            TAB_BROSWER -> if (broswerRouterFragment == null) {
                broswerRouterFragment = BroswerRouterFragment()
                transaction.add(R.id.frame_container, broswerRouterFragment!!)
            } else {
                showFragment(transaction, broswerRouterFragment)
            }

            TAB_MINE -> if (mineFragment == null) {
                mineFragment = MineFragment()
                transaction.add(R.id.frame_container, mineFragment!!)
            } else {
                showFragment(transaction, mineFragment)
            }
        }
        transaction.commit()
    }

    /**
     * 隐藏fragment
     *
     * @param transaction
     */
    private fun hideFragment(transaction: FragmentTransaction) {
        homeFragment?.let {
            transaction.hide(it)
        }
        broswerRouterFragment?.let {
            transaction.hide(it)
        }
        mineFragment?.let {
            transaction.hide(it)
        }
    }

    /**
     * 显示fragment
     *
     * @param transaction
     */
    private fun showFragment(transaction: FragmentTransaction, fragment: Fragment?) {
        fragment?.let {
            transaction.show(it)
        }
    }



}
