package com.privacy.browser.repository.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.orhanobut.logger.Logger
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.pojo.SearchHistory
import kotlinx.coroutines.flow.Flow

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/21 15:40
 * version :
 * desc    :
 **/

@Dao
interface BrowserHistoryDao {

    /**
     * 插入一条历史数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(browserHistory: BrowserHistory)

    /**
     * 删除所有历史数据
     */
    @Query("delete from browser_history")
    suspend fun deleteAll()

    @Query("delete from browser_history where id = :id")
    suspend fun deleteById(id: Long)


    @Query("select COUNT(1) from browser_history where id = :id")
    suspend fun getCountById(id: Long): Int

    @Query("select COUNT(1) from browser_history")
    suspend fun getTotalCount(): Int

    /**
     * 获取历史数据对应的[Flow]
     * @param size 获取历史纪录的条数
     * @param offset 指针开始位置
     */
    @Query("select * from browser_history order by timestamp desc limit :size offset :offset")
    fun getHistoryFlow(size: Int, offset: Int): Flow<List<BrowserHistory>?>


    // 执行 SQL 文件中的 DML 语句  执行批量数据
    @Transaction
    fun executeSqlFileStatements(db: SupportSQLiteDatabase, sqlStatements: List<String>) {
        var count: Int = 1
        for (statement in sqlStatements) {
            if (!statement.isNullOrEmpty()) {
                db.execSQL(statement)
            }
            Log.e("TAG", "executeSqlFileStatements: $count    $statement")
            count++
        }
        Logger.e("sql执行完成")
    }


}