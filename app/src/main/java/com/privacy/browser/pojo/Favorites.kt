package com.privacy.browser.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * author  : WYH
 * e-mail  : wenyuhang@qinjiakonggu.com
 * date    : 2024/6/21 17:54
 * version : 1.0.0
 * desc    : 历史浏览记录
 **/

@Entity(tableName = "favorites",indices = [Index(value = ["web_link"], unique = true)])
data class Favorites(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Long = 0,

    @ColumnInfo(name = "web_title", typeAffinity = ColumnInfo.TEXT)
    val webTitle: String,

    @ColumnInfo(name = "web_link", typeAffinity = ColumnInfo.TEXT)
    val webLink: String,

    @ColumnInfo(name = "timestamp", typeAffinity = ColumnInfo.TEXT)
    val timestamp: String,
)