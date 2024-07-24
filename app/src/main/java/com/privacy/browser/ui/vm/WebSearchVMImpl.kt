package com.privacy.browser.ui.vm

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.privacy.browser.repository.api.ApiService
import com.privacy.browser.config.Constants
import com.privacy.browser.repository.database.AppDataBase
import com.privacy.browser.pojo.SearchHistory
import com.privacy.browser.ui.base.BaseViewModel
import com.wlwork.libframe.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import java.net.URLEncoder
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
) : BaseViewModel(application) {


    // 数据库
    private val searchHistoryDao by lazy {
        repository.getRoomDatabase(AppDataBase::class.java).searchHistoryDao
    }


    // 页面当前title
    val pageWebTitle: MutableLiveData<String> = MutableLiveData()

    // 搜索内容
    val editSearchContent: MutableLiveData<String> = MutableLiveData()

    // 页面当前地址
    val pageWebUrl: MutableLiveData<String> = MutableLiveData()

    private val baseSearchUrl = "https://www.baidu.com/s?wd="

    /**
     * 取参
     */
    fun getIntentParam(intent: Intent): Intent {
        val webTitle = intent.getStringExtra(Constants.WEB_TITLE).orEmpty()
        val webUrl = intent.getStringExtra(Constants.WEB_LINK).orEmpty()
        pageWebTitle.postValue(webTitle)
        pageWebUrl.postValue(webUrl)
        return intent
    }

    /**
     * 获取搜索链接
     */
    fun getSearchLink(): String {
        var result: String
        val searchContent = editSearchContent.value.orEmpty()
        result = if (searchContent.startsWith("http:") || searchContent.startsWith("https:")) {
            insertSearchHistory(searchContent)
            searchContent
        } else {
            val value = searchContent.trim()
            insertSearchHistory(value)
            baseSearchUrl + URLEncoder.encode(value, "UTF-8")
        }
        Logger.d("搜索: $result")
        return result
    }

    /**
     * 获取当前页面的地址
     */
    fun getWebUrl(): String {
        return pageWebUrl.value.orEmpty()
    }

    /**
     * 发送数据到编辑框
     */
    fun postEditSearchContent(value: String) {
        editSearchContent.postValue(value)
    }




    /**
     * 插入搜索历史
     */
    private fun insertSearchHistory(word: String) {
        launch {
            if (word.isNotEmpty()) {
                searchHistoryDao.insert(
                    SearchHistory(
                        searchEngine = 1,
                        word = word,
                        timestamp = System.currentTimeMillis().toString()
                    )
                )
            }
        }
    }

    /**
     * 获取搜索历史
     */
    fun getHistory() = searchHistoryDao.getHistoryFlow(30).filterNotNull()



    /*************************************************************************/

    private val apiService: ApiService by lazy {
        repository.getRetrofitService(ApiService::class.java)
    }

    private val _oilPriceFlow = MutableStateFlow<List<SearchHistory>>(emptyList())
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
        Logger.d("getOilPriceInfo() 执行了")
    }
}