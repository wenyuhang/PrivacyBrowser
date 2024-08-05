package com.privacy.browser.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.privacy.browser.pojo.BrowserHistory
import com.privacy.browser.pojo.Favorites
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
interface FavoritesDao {

    /**
     * 插入一条历史数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorites: Favorites)

    /**
     * 删除所有历史数据
     */
    @Query("delete from favorites")
    suspend fun deleteAll()

    /**
     * 根据id 删除数据
     */
    @Query("delete from favorites where web_link = :link")
    suspend fun deleteByLink(link: String)



    @Query("select COUNT(1) from favorites")
    suspend fun getTotalCount(): Int
    /**
     * 获取历史数据对应的[Flow]
     * @param size 获取历史纪录的条数
     * @param offset 指针开始位置
     */
    @Query("select * from favorites order by timestamp desc limit :size offset :offset")
    fun getHistoryFlow(size: Int,offset: Int): Flow<List<Favorites>?>

    @Query("SELECT COUNT(1) FROM favorites WHERE web_link = :webLink")
    suspend fun countByWebLink(webLink: String): Int
}