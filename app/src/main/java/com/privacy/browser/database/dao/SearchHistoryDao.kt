package com.privacy.browser.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
interface SearchHistoryDao {

    /**
     * 插入一条历史数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistory)

    /**
     * 删除所有历史数据
     */
    @Query("delete from search_history")
    suspend fun deleteAll()

    /**
     * 获取历史数据对应的[Flow]
     * @param size 获取历史纪录的条数
     */
    @Query("select * from search_history order by timestamp desc limit :size")
    fun getHistoryFlow(size: Int): Flow<List<SearchHistory>?>
}