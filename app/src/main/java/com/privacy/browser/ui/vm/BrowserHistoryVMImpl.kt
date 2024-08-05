package com.privacy.browser.ui.vm


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.orhanobut.logger.Logger
import com.privacy.browser.pojo.BrowserHistory
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
 * date    : 2024/6/20 10:12
 * version : 1.0.0
 * desc    :
 **/

@HiltViewModel
class BrowserHistoryVMImpl @Inject constructor(
    private val repository: Repository,
) : BaseViewModel() {

    @Inject
    lateinit var pageResult: PageResult<List<BrowserHistory>>

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
                pageResult.setPageData(it,curPage,pageSize,totalCount,(pageSize*curPage)<totalCount)
                browserHistoryLiveData.postValue(pageResult)
            }
        }
    }

    /**
     *
     */
    fun deleteHistoryById(id: Long){
        viewModelScope.launch {
            val count = browserHistoryDao.getCountById(id)
            if (count>0){
                browserHistoryDao.deleteById(id)
                val queryCount = browserHistoryDao.getCountById(id)
                if (queryCount>0){
                    sendMessage("删除失败")
                }else{
                    sendMessage("删除成功")
                }

            }else{
                sendMessage("数据不存在")
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


    /**
     * 执行批量数据  慎用!!!
     */
    @Transaction
    fun executeSql(statements : List<String>){
        viewModelScope.launch {
            Logger.e("statements size: ${statements.size} $browserHistoryDao")
            val db = repository.getRoomDatabase(AppDataBase::class.java).openHelper.writableDatabase
//            browserHistoryDao.executeSqlFileStatements(
//                db,
//                statements
//            )
            for (statement in statements) {
                if (!statement.isNullOrEmpty()) {
                    db.execSQL(statement)
                }
            }
            Logger.e("sql执行完成")

//            db.execSQL("INSERT INTO \"browser_history\" VALUES (641, 1, '17c', 'https://www.iujvqw.xyz:8888/40.html', 1722357373843)")
        }
    }
}