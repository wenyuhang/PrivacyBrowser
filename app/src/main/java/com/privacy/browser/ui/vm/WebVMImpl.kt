package com.privacy.browser.ui.vm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.privacy.browser.component.overview.WebTabManager
import com.privacy.browser.config.Constants
import com.privacy.browser.repository.database.AppDataBase
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.ui.WebSearchActivity
import com.wlwork.libframe.base.BaseViewModel
import com.wlwork.libframe.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 10:12
 * version : 1.0.0
 * desc    :
 **/

@HiltViewModel
class WebVMImpl @Inject constructor(
    private val repository: Repository,
) : BaseViewModel() {

    private val baseEngineUrl = "https://www.baidu.com/"
    // 页面当前title
    val pageWebTitle: MutableLiveData<String> = MutableLiveData()
    // 页面当前地址
    private val pageWebUrl: MutableLiveData<String> = MutableLiveData()
    // 页面数量
    val pageSize: MutableLiveData<String> = MutableLiveData()


    init {
        postWebTitle(baseEngineUrl)
    }

    // 数据库
    private val browserHistoryDao by lazy {
        repository.getRoomDatabase(AppDataBase::class.java).browserHistoryDao
    }

    /**
     * 插入搜索历史
     */
    fun insertSerarchHistory(title: String?,link: String?) {
        viewModelScope.launch {
            if (checkLink(link)) {
                browserHistoryDao.insert(
                    BrowserHistory(
                        searchEngine = 1,
                        webTitle = title.orEmpty(),
                        webLink = link!!,
                        timestamp = System.currentTimeMillis().toString()
                    )
                )
                Logger.d("历史数据已入库")
            }
        }
    }

    /**
     * 检查链接
     * 1. baseEngineUrl 不做入库处理
     */
    private fun checkLink(link: String?):Boolean {
        link?.let {
            if (it.isNotEmpty() && baseEngineUrl != it){
                return true
            }
        }
        return false
    }

    /**
     * 传递当前地址
     */
    fun postWebUrl(url: String?){
        url?.let {
            pageWebUrl.postValue(it)
        }
    }

    /**
     * 获取当前页面地址
     */
    private fun getWebUrl():String{
        return pageWebUrl.value.orEmpty()
    }

    /**
     * 传递当前页title
     */
    fun postWebTitle(title: String?){
        title?.let {
            pageWebTitle.postValue(it)
        }
    }


    /**
     * 传递当前webview数量
     */
    fun postWebSize(size: Int?){
        size?.let {
            pageSize.postValue(it.toString())
        }
    }

    fun getBuildIntent(activity: Activity): Intent {
        val intent = Intent(activity, WebSearchActivity::class.java)
        intent.putExtra(Constants.WEB_TITLE,pageWebTitle.value.orEmpty())
        intent.putExtra(Constants.WEB_LINK,getWebUrl())
        return intent
    }




    fun catchWebView(context: Context){
        val wv_capture = WebTabManager.getInstance().getCacheWebTab().last().webView
        wv_capture.isDrawingCacheEnabled = true
        val bitmap = wv_capture.drawingCache
//        val bitmap = Bitmap.createBitmap(wv_capture.width, wv_capture.height, Bitmap.Config.RGB_565)
//        val canvas = Canvas(bitmap)
//        wv_capture.draw(canvas)
        val currentBitmap = BitmapDrawable(context.resources, bitmap).current
        currentBitmap?.let {
            WebTabManager.getInstance().getCacheWebTab().last().picture = it
        }
    }
}