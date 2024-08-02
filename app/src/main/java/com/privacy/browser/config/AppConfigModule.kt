package com.privacy.browser.config

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.king.retrofit.retrofithelper.RetrofitHelper
import com.orhanobut.logger.Logger
import com.wlwork.libframe.config.AppliesOptions
import com.wlwork.libframe.config.FrameConfigModule
import com.wlwork.libframe.di.module.ConfigModule

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/8/1 15:17
 * version :
 * desc    :
 **/

class AppConfigModule : FrameConfigModule(){
    override fun applyOptions(context: Context, builder: ConfigModule.Builder) {
        // 如果你没有使用FrameConfigModule中的第一中方式初始化BaseUrl，也可以通过第二种方式来设置BaseUrl（二选其一即可）
        builder.baseUrl(Constants.BASE_URL)

//        builder.roomDatabaseOptions(object : AppliesOptions.RoomDatabaseOptions{
//            override fun applyOptions(builder: RoomDatabase.Builder<out RoomDatabase>) {
////                builder.addCallback(object : RoomDatabase.Callback(){
////                    override fun onCreate(db: SupportSQLiteDatabase) {
////                        super.onCreate(db)
////                    }
////                })
//                Logger.e("roomDatabaseOptions 执行了")
//            }
//        })
    }
}