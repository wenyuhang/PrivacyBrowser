package com.wlwork.libframe.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/19 16:23
 * version :
 * desc    : 如果您继承使用了 BaseViewModel 或其子类，你需要参照如下方式添加 @HiltViewModel 和 @Inject 注解标记，来进行注入
 *           ```
 *           // 示例
 *           @HiltViewModel
 *           class YourViewModel @Inject constructor(): BaseViewModel() {
 *
 *           }
 *           ```
 **/

@HiltViewModel
open class BaseViewModel @Inject constructor(): ViewModel(){
    /**
     * 加载状态
     */
    private val _loadingState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    /**
     * 加载状态
     */
    val loadingState: Flow<Boolean> = _loadingState.asStateFlow()

    /**
     * 消息事件
     */
    private val _messageEvent: MutableSharedFlow<String> = MutableSharedFlow()

    /**
     * 消息事件
     */
    val messageEvent: Flow<String> = _messageEvent.asSharedFlow()

    /**
     * 显示加载状态
     */
    fun showLoading() {
        _loadingState.tryEmit(true)
    }

    /**
     * 隐藏加载状态
     */
    fun hideLoading() {
        _loadingState.tryEmit(false)
    }

    /**
     * 发送消息；如果消息内容不为空，则将消息发送到页面，通过[IView.showToast]进行显示
     * @param message 消息内容
     */
    fun sendMessage(message: String?) {
        message?.also {
            viewModelScope.launch {
                _messageEvent.emit(it)
            }
        }
    }
}