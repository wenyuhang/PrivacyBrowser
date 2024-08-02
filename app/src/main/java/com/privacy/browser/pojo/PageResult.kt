package com.privacy.browser.pojo

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/7/24 16:21
 * version : 1.0.0
 * desc    : 分页数据
 **/

class PageResult<T> {
    var listData: T? = null
    var curPage: Int = 1
    var pageSize: Int = 0
    var total: Int = 0
    var isLoadMore: Boolean = false

    constructor()

    fun setPageData(listData: T, curPage: Int, pageSize: Int, total: Int, isLoadMore: Boolean) {
        this.listData = listData
        this.curPage = curPage
        this.pageSize = pageSize
        this.total = total
        this.isLoadMore = isLoadMore
    }
}

