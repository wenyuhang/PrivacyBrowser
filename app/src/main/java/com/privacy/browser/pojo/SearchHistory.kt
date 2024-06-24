package com.privacy.browser.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/20 9:42
 * version : 1.0.0
 * desc    : 检索历史
 **/

/**
 * @Entity：这是Room用来标记一个类作为数据库表的注解。它可以包含一些参数，比如tableName来指定表的名称。
 * indices：这是@Entity注解的一个参数，用于定义一个或多个索引。索引可以提高查询性能，特别是在大型数据库中。
 * Index：这是一个构造索引的类，其参数value表示要创建索引的列名，unique参数则表示该索引是否需要唯一性约束。
 * 这意味着你正在定义一个带有单个唯一索引的实体类，该索引基于word字段。这个索引将确保word字段的值在整张表中是唯一的，即不会有两条记录拥有相同的word值。
 */

//@Entity(tableName = "search_history")
@Entity(tableName = "search_history",indices = [Index(value = ["word"], unique = true)])
data class SearchHistory(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Long = 0,

    @ColumnInfo(name = "search_engine", typeAffinity = ColumnInfo.INTEGER)
    val searchEngine: Int,

    @ColumnInfo(name = "word", typeAffinity = ColumnInfo.TEXT)
    val word: String,

    @ColumnInfo(name = "timestamp", typeAffinity = ColumnInfo.TEXT)
    val timestamp: String
)
