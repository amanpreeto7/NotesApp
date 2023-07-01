package com.o7solutions.notesapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * @Author: 017
 * @Date: 01/07/23
 * @Time: 9:18 am
 */
@Entity(foreignKeys = [
    ForeignKey(entity = Notes::class,
    parentColumns = ["id"],
    childColumns = ["notesId"])])
class TodoEntity {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

    @ColumnInfo()
    var notesId : Int = 0

    @ColumnInfo()
    var task: String? = null

    @ColumnInfo()
    var isCompleted: Boolean? = null
}