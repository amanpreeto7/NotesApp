package com.o7solutions.notesapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Author: 017
 * @Date: 24/06/23
 * @Time: 10:19 am
 */
@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true)
    var id: Int= 0,
    @ColumnInfo
    var title: String?= null,
    @ColumnInfo()
    var description: String?= null,
    @ColumnInfo()
    var image: String?= null,
)
