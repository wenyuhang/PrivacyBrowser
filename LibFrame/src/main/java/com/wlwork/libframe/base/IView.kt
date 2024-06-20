package com.wlwork.libframe.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/19 16:20
 * version :
 * desc    : 视图层定义一些共性需要实现的函数
 **/

interface IView<VM> {
    /**
     * 根布局ID
     * @return
     */
    @LayoutRes
    fun getLayoutId(): Int

    /**
     * 初始化数据
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 是否使用 ViewDataBinding
     */
    fun isBinding(): Boolean

    /**
     * 创建 [ViewModel]
     */
    fun createViewModel(): VM

    /**
     * 显示加载中
     */
    fun showLoading()

    /**
     * 隐藏加载中
     */
    fun hideLoading()

    /**
     * 通过[Toast]显示提示信息
     */
    fun showToast(text: CharSequence)

    /**
     * 通过[Toast]显示提示信息
     */
    fun showToast(@StringRes resId: Int)
}