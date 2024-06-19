package com.privacy.browser.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/18 10:53
 * version : 1.0.0
 * desc    :
 **/

class BrowserVMImpl : ViewModel(){


    // 页面当前title
    val editPageWebTitle: MutableLiveData<String> = MutableLiveData()
    // 页面当前地址
    private val editPageWebUrl: MutableLiveData<String> = MutableLiveData()


    /**
     * 传递当前地址
     */
    fun postWebUrl(url: String?){
        url?.let {
            editPageWebUrl.postValue(url)
        }
    }

    /**
     * 获取当前页面地址
     */
    fun getWebUrl():String{
        return editPageWebUrl.value.orEmpty()
    }

    /**
     * 传递当前页title
     */
    fun postWebTitle(url: String?){
        url?.let {
            editPageWebTitle.postValue(url)
        }
    }

    fun getWebTitle():String{
        return editPageWebTitle.value.orEmpty()
    }
}