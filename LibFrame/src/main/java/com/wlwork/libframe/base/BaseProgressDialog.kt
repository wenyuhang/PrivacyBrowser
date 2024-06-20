package com.wlwork.libframe.base

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import com.wlwork.libframe.R

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/19 17:05
 * version :
 * desc    : 对话框基类
 **/

open class BaseProgressDialog : Dialog {
    constructor(context: Context) : this(context, R.style.mvvmframe_progress_dialog)
    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        window?.attributes?.gravity = Gravity.CENTER
    }

}