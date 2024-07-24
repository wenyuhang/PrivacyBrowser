package com.privacy.browser.pojo

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/7/24 16:21
 * version : 1.0.0
 * desc    : 分页数据
 **/

data class PageResult<T>(

    val listData : T,
    val curPage  : Int,
    val pageSize : Int,
    val total    : Int,
    val isLoadMore: Boolean
)
