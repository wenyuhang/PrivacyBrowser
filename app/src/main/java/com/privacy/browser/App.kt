package com.privacy.browser

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.room.Room
import androidx.room.migration.Migration
import com.king.retrofit.retrofithelper.RetrofitHelper
import com.privacy.browser.config.Constants
import com.privacy.browser.repository.database.AppDataBase
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.wlwork.libframe.base.BaseApplication
import dagger.hilt.android.HiltAndroidApp

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/30 12:00
 * version : 1.0.0
 * desc    : application
 **/
@HiltAndroidApp
class App : BaseApplication(){
    lateinit var curActivity: Activity
    companion object {
        //生产环境、开发环境区分 true:开发 false:生产
        var envConfig: Boolean = BuildConfig.DEBUG

        lateinit var app: App
    }

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, R.color.white)
            MaterialHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context)
        }

    }

    override fun onCreate() {
        super.onCreate()
        app = this

        initConfig()
        // 如果你没有使用FrameConfigModule中的第一中方式初始化BaseUrl，也可以通过第二种方式来设置BaseUrl（二选其一即可）
//        RetrofitHelper.getInstance().setBaseUrl(Constants.BASE_URL)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                curActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }

        })

//        Room.databaseBuilder(applicationContext,AppDataBase::class.java,"room.dp").addMigrations(*AppDataBase.getMigrations().toVarArgsArray()).build()
    }
    fun List<Migration>.toVarArgsArray(): Array<Migration> = toList().toTypedArray()

    /**
     * 初始化配置
     */
    private fun initConfig() {
        // 获取配置环境
        envConfig = BuildConfig.DEBUG
        // 初始化log
        initLogger()
    }

    /**
     * 初始化log
     */
    private fun initLogger() {
        val formatStrategy: com.orhanobut.logger.FormatStrategy =
            com.orhanobut.logger.PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true) // (Optional) Whether to show thread info or not. Default true
                .methodCount(6) // (Optional) How many method line to show. Default 2
                .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
                .tag(Constants.LOGGER_TAG) // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        com.orhanobut.logger.Logger.addLogAdapter(object :
            com.orhanobut.logger.AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return envConfig
            }
        })
    }

}