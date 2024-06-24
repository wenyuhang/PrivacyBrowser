package com.privacy.browser.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.privacy.browser.database.dao.BrowserHistoryDao
import com.privacy.browser.database.dao.SearchHistoryDao
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.pojo.SearchHistory

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/21 15:39
 * version :
 * desc    :
 **/

@Database(entities = [BrowserHistory::class,SearchHistory::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase(){

    abstract val browserHistoryDao: BrowserHistoryDao

    abstract val searchHistoryDao: SearchHistoryDao
}