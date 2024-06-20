package com.wlwork.libframe.utils

import android.content.Intent
import android.os.SystemClock
import timber.log.Timber

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/19 16:40
 * version :
 * desc    : 跳转防抖
 **/

// internal 是一个访问修饰符（visibility modifier），它用于控制代码的可见性和可访问性。internal 的主要目的是提供一种机制，使得代码可以在同一模块内部共享，但对外部世界不可见。
internal class JumpDebounce {

    private var lastTag: String? = null
    private var lastJumpTime: Long = 0L

    /**
     * 是否忽略跳转
     */
    fun isIgnoreJump(intent: Intent, intervalTime: Int = DEBOUNCE_INTERVAL_TIME): Boolean {
        val jumpTag = if (intent.component != null) {
            intent.component!!.className
        } else if (intent.action != null) {
            intent.action
        } else {
            null
        }
        if (jumpTag.isNullOrEmpty()) {
            return false
        }
        if (jumpTag == lastTag && lastJumpTime > SystemClock.elapsedRealtime() - intervalTime) {
            Timber.d("Ignore Intent:$jumpTag")
            return true
        }
        lastTag = jumpTag
        lastJumpTime = SystemClock.elapsedRealtime()
        return false
    }

    companion object {
        const val DEBOUNCE_INTERVAL_TIME = 500
    }
}