package com.wlwork.libframe.base

import android.content.Context
import android.content.res.Resources

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/19 17:04
 * version :
 * desc    : 扩展函数
 **/
/**
 * 通过id获取对应的String资源
 */
internal fun Context.stringResIdToString(id: Int): String {
    return resources.stringResIdToString(id)
}

/**
 * 通过id获取对应的String资源
 */
internal fun Resources.stringResIdToString(id: Int): String {
    return try {
        this.getString(id)
    } catch (e: Resources.NotFoundException) {
        // 当找不到ID对应的资源时，则直接返回ID本身
        return id.toString()
    }
}
