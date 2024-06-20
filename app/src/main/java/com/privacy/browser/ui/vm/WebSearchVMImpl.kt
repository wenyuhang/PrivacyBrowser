package com.privacy.browser.ui.vm

import android.app.Application
import com.orhanobut.logger.Logger
import com.privacy.browser.api.ApiService
import com.privacy.browser.pojo.SearchHistoryBean
import com.privacy.browser.ui.base.BaseViewModel
import com.wlwork.libframe.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 10:12
 * version : 1.0.0
 * desc    :
 **/

@HiltViewModel
class WebSearchVMImpl @Inject constructor(
    private val repository: Repository,
    application: Application
) : BaseViewModel(application){

//    private val apiService: ApiService by lazy {
//        repository.getRetrofitService(ApiService::class.java)
//    }

    private val _oilPriceFlow = MutableStateFlow<List<SearchHistoryBean>>(emptyList())
    val oilPriceFlow = _oilPriceFlow.asSharedFlow()

    /**
     * 获取油价信息
     */
    fun getOilPriceInfo() {
//        launch {
//            val result = apiService.getOilPriceInfo()
//            if (isSuccess(result)) {
//                result.data?.also {
//                    _oilPriceFlow.emit(it)
//                }
//            }
//        }
        Logger.d("执行了")
    }
}