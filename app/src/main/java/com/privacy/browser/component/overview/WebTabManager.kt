package com.privacy.browser.component.overview

import android.content.MutableContextWrapper
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.privacy.browser.App
import com.privacy.browser.R
import java.util.LinkedList

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/25 16:41
 * version :
 * desc    :
 */
class WebTabManager private constructor(){

    private object Holder{
        val instance = WebTabManager()
    }

    companion object Instance {
        var increaseKey = 0
        @JvmStatic
        fun getInstance(): WebTabManager{
            return Holder.instance
        }
    }

    val cacheWebTab = LinkedList<WebBean>()

    fun divideAllWebView(){
        for (wb in cacheWebTab){
            wb.webView.parent?.let {
                (it as ViewGroup).removeAllViews()
            }
        }
    }

    fun getCacheWebTab(): List<WebBean>{
        return cacheWebTab
    }

    fun addNewTab(): WebBean {
        val webView = CommonWebView(MutableContextWrapper(App.app))
        val wb = WebBean(increaseKey++, webView, "首页", ContextCompat.getDrawable(App.app, R.drawable.ic_launcher_foreground), ContextCompat.getDrawable(App.app, R.drawable.ic_launcher_foreground))
        cacheWebTab.add(wb)
        return wb
    }

    fun joinWebTabToLast(key: Int){
        for (wb in cacheWebTab){
            if (wb.id == key){
                cacheWebTab.remove(wb)
                cacheWebTab.addLast(wb)
                break
            }
        }
    }

    fun updateLastTabTitle(tabTitle: String){
        val wb = cacheWebTab.last
        wb.tabTitle = tabTitle
    }

    fun updateLastTabIcon(tabIcon: Drawable){
        val wb = cacheWebTab.last
        wb.tabIcon = tabIcon
    }
}