package com.privacy.browser


/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2023/11/10 12:12
 * version : 1.0.0
 * desc    : Local
 */
interface Constant {
//    interface RequestUrl {
//        companion object {
//            // Base域名
//            const val BASE_URL = BuildConfig.BASE_URL
//
//            // web启动页
//            const val INDEX_URL = BuildConfig.BASE_WEB_URL + "/efipeso/index.html"
//
//            // 行为埋点
//            const val addRecord = "/app/usuario/conductaUsuario"
//        }
//    }


    interface SP {
        companion object {
            const val IS_INSTALL = "readSta"
            const val DEVICE_ID = "phoneFing"
        }
    }

    companion object {
        //包名
        const val PACKAGE_NAME = "com_privacy_browser"

        // Log Tag
        const val LOGGER_TAG =  "TAG_"+PACKAGE_NAME

//        // fileprovider ( 用于相机拍照 )
//        const val APP_FILEPROVIDER = BuildConfig.FILE_PROVIDER
    }
}