package com.privacy.browser.repository.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import com.privacy.browser.repository.database.dao.BrowserHistoryDao
import com.privacy.browser.repository.database.dao.SearchHistoryDao
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.pojo.Favorites
import com.privacy.browser.pojo.SearchHistory
import com.privacy.browser.repository.database.dao.FavoritesDao

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/21 15:39
 * version :
 * desc    :
 **/
//@Database(entities = [BrowserHistory::class,SearchHistory::class], version = 1)
@Database(entities = [BrowserHistory::class,SearchHistory::class,Favorites::class], version = 2, autoMigrations = [AutoMigration(from = 1, to = 2)])
abstract class AppDataBase : RoomDatabase(){

    abstract val browserHistoryDao: BrowserHistoryDao

    abstract val searchHistoryDao: SearchHistoryDao

    abstract val favoritesDao: FavoritesDao

    //    companion object {
    //        private val MIGRATION_1_2 = 1 to 2
    //
    //        fun getMigrations(): List<Migration> {
    //            return listOf(
    //                Migration(MIGRATION_1_2.first, MIGRATION_1_2.second) {database ->
    //                    // 执行迁移逻辑
    //                    database.execSQL("CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT, web_title TEXT NOT NULL, web_link TEXT NOT NULL UNIQUE, timestamp TEXT NOT NULL)")
    //                }
    //            )
    //        }
    //    }
}