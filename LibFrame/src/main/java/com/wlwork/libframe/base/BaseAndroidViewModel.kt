package com.wlwork.libframe.base

import android.app.Application
import androidx.annotation.StringRes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/19 17:06
 * version :
 * desc    : 如果您继承使用了 BaseAndroidViewModel 或其子类，你需要参照如下方式添加 @HiltViewModel 和 @Inject 注解标记，来进行注入
 *          ```
 *          // 示例
 *          @HiltViewModel
 *          class YourViewModel @Inject constructor(): BaseAndroidViewModel() {
 *
 *          }
 *          ```
 **/

@HiltViewModel
open class BaseAndroidViewModel @Inject constructor(private val application: Application) :
    BaseViewModel() {

    /**
     * 获取[Application]
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Application> getApplication(): T {
        return application as T
    }

    /**
     * 发送消息；如果消息内容不为空，则将消息发送到页面，通过[IView.showToast]进行显示
     * @param resId 消息资源ID
     */
    fun sendMessage(@StringRes resId: Int) {
        sendMessage(application.stringResIdToString(resId))
    }
}