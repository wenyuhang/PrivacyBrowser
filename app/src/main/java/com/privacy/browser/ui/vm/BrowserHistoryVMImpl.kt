package com.privacy.browser.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.pojo.PageResult
import com.privacy.browser.repository.database.AppDataBase
import com.wlwork.libframe.base.BaseViewModel
import com.wlwork.libframe.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 10:12
 * version : 1.0.0
 * desc    :
 **/

@HiltViewModel
class BrowserHistoryVMImpl @Inject constructor(
    private val repository: Repository,
) : BaseViewModel() {


    private val browserHistoryDao by lazy {
        repository.getRoomDatabase(AppDataBase::class.java).browserHistoryDao
    }

    val browserHistoryLiveData by lazy { MutableLiveData<PageResult<List<BrowserHistory>>>() }


//    fun getHistory() = browserHistoryDao.getHistoryFlow(1,10).filterNotNull()

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
            val totalCount = browserHistoryDao.getTotalCount()

            Logger.e("pageSize===>$pageSize ===>$curPage offset==>$offset")
            browserHistoryDao.getHistoryFlow(pageSize,offset).filterNotNull().collect{
                Logger.e("===>$totalCount ==>"+it.size)
                val result =  PageResult(it,curPage,pageSize,totalCount,(pageSize*curPage)<totalCount)
                browserHistoryLiveData.postValue(result)
            }
        }
    }

    /**
     * 协程
     */
    fun launch(
        showLoading: Boolean,
        block: suspend () -> Unit,
        error: suspend (Throwable) -> Unit
    ) = viewModelScope.launch {
        try {
            if (showLoading) {
                showLoading()
            }
            block()
        } catch (e: Throwable) {
            error(e)
        }
        if (showLoading) {
            hideLoading()
        }
    }

    /**
     * 协程
     */
    fun launch(showLoading: Boolean = true, block: suspend () -> Unit) =
        launch(showLoading, block) {
            Timber.w(it)
//            if (NetworkUtils.isNetWorkActive(getApplication())) {
//                when (it) {
//                    is SocketTimeoutException -> sendMessage(R.string.result_connect_timeout_error)
//                    is ConnectException -> sendMessage(R.string.result_connect_failed_error)
//                    else -> sendMessage(R.string.result_error)
//                }
//            } else {
//                sendMessage(R.string.result_network_unavailable_error)
//            }
        }
}