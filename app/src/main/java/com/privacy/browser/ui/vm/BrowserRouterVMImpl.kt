package com.privacy.browser.ui.vm

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.privacy.browser.App
import com.privacy.browser.R
import com.privacy.browser.component.overview.RecentListInterface
import com.privacy.browser.component.overview.WebBean
import com.privacy.browser.component.overview.WebTabManager
import com.privacy.browser.component.overview.model.OverviewAdapter
import com.privacy.browser.component.overview.model.ViewHolder
import com.privacy.browser.component.overview.views.Overview
import com.privacy.browser.ui.WebActivity
import com.wlwork.libframe.base.BaseViewModel
import com.wlwork.libframe.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 10:12
 * version : 1.0.0
 * desc    :
 **/

@HiltViewModel
class BrowserRouterVMImpl @Inject constructor(
    private val repository: Repository,
) : BaseViewModel() {

    companion object Adapter {
        @JvmStatic
        @BindingAdapter("setAdapter")
        fun setAdapter(rl: Overview, command: RecentListInterface) {
            command.execute(rl)
        }
    }

    // 是否显示Overview
    val isVisableOverview: MutableLiveData<Boolean> = MutableLiveData()

    val recentListCommand: RecentListInterface = object : RecentListInterface {
        override fun execute(layout: Overview) {
            setStackAdapter(layout)

            layout.setCallbacks(object : Overview.RecentsViewCallbacks {
                override fun onCardDismissed(position: Int) {
                }

                override fun onAllCardsDismissed() {
                }

            })
            onClick(layout)
        }
    }

    init {
        isVisableOverview.postValue(false)
    }

    private fun setStackAdapter(layout: Overview) {
        val models: List<WebBean> = WebTabManager.getInstance().getCacheWebTab()
        layout.setTaskStack(object : OverviewAdapter<ViewHolder<View, WebBean>, WebBean>(models) {
            override fun onCreateViewHolder(
                context: Context?,
                parent: ViewGroup?
            ): ViewHolder<View, WebBean> {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.recent_tab_card, parent, false)
                return ViewHolder<View, WebBean>(view)
            }

            override fun onBindViewHolder(vh: ViewHolder<View, WebBean>?, position: Int) {
                vh?.let {
                    val wb = it.model
                    it.itemView.findViewById<TextView>(R.id.tab_title).text = wb.tabTitle
                    it.itemView.findViewById<ImageView>(R.id.tab_icon).setImageDrawable(wb.tabIcon)
                    val recentContent = it.itemView.findViewById<ImageView>(R.id.recent_content)
                    wb.picture?.let {picture ->
                        Logger.i("picture地址： $picture")
                        recentContent.setImageDrawable(picture)
                    }

                    it.itemView.setOnClickListener {
                        WebTabManager.getInstance().joinWebTabToLast(wb.id)
                        val activity = App.app.curActivity
                        val intent = Intent(activity, WebActivity::class.java)
                        activity.startActivity(intent)
                    }

                }

            }

        })
    }

    fun jumpWebPage(){
        onClick(null)
        isVisableOverview.postValue(true)
    }

    fun onClick(view: View?) {
        WebTabManager.getInstance().divideAllWebView()
        WebTabManager.getInstance().addNewTab()
        val activity = App.app.curActivity
        val intent = Intent(activity, WebActivity::class.java)
        activity.startActivity(intent)
        isVisableOverview.postValue(true)
    }

    fun updateRecentList(layout: Overview) {
        isVisableOverview.postValue(true)
        setStackAdapter(layout)
    }
}