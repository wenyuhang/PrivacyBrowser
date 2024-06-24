package com.privacy.browser

import android.app.Application
import com.king.retrofit.retrofithelper.RetrofitHelper
import com.privacy.browser.config.Constants
import dagger.hilt.android.HiltAndroidApp

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/1/30 12:00
 * version : 1.0.0
 * desc    : application
 **/
@HiltAndroidApp
class App : Application(){

    companion object {
        //生产环境、开发环境区分 true:开发 false:生产
        var envConfig: Boolean = BuildConfig.DEBUG
    }

    override fun onCreate() {
        super.onCreate()
        initConfig()
        // 如果你没有使用FrameConfigModule中的第一中方式初始化BaseUrl，也可以通过第二种方式来设置BaseUrl（二选其一即可）
        RetrofitHelper.getInstance().setBaseUrl(Constants.BASE_URL)


    }

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