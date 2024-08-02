package com.privacy.browser.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.pojo.Favorites
import com.privacy.browser.pojo.PageResult
import com.privacy.browser.repository.database.AppDataBase
import com.wlwork.libframe.base.BaseViewModel
import com.wlwork.libframe.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/7/26 17:26
 * version : 1.0.0
 * desc    :
 **/

@HiltViewModel
class FavoritesVMImpl @Inject constructor(
    private val repository: Repository
) : BaseViewModel(){

    @Inject
    lateinit var pageResult: PageResult<List<Favorites>>

    private val favoritesDao by lazy {
        repository.getRoomDatabase(AppDataBase::class.java).favoritesDao
    }

    val favoritesLiveData by lazy { MutableLiveData<PageResult<List<Favorites>>>() }

    /**
     * 查询历史数据
     */
    fun getRequestData(curPage: Int,pageSize: Int) {
        viewModelScope.launch {
            // 汇总查询条件
            var offset = 0
            if (curPage>1) {
                offset = (curPage-1)*pageSize
            }
            // 查询总数
            val totalCount = favoritesDao.getTotalCount()

            Logger.e("pageSize===>$pageSize ===>$curPage offset==>$offset")
            favoritesDao.getHistoryFlow(pageSize,offset).filterNotNull().collect{
                Logger.e("===>$totalCount ==>"+it.size)
                pageResult.setPageData(it,curPage,pageSize,totalCount,(pageSize*curPage)<totalCount)
                favoritesLiveData.postValue(pageResult)
            }
        }
    }

}