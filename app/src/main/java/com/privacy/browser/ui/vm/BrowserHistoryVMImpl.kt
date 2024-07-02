package com.privacy.browser.ui.vm

import com.privacy.browser.repository.database.AppDataBase
import com.wlwork.libframe.base.BaseViewModel
import com.wlwork.libframe.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
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


    fun getHistory() = browserHistoryDao.getHistoryFlow(10).filterNotNull()
}