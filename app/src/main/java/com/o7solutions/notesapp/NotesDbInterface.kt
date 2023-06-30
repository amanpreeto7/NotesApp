package com.o7solutions.notesapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * @Author: 017
 * @Date: 28/06/23
 * @Time: 8:59 am
 */
@Dao
interface NotesDbInterface {

    @Insert
    fun insertNotes(notes: Notes)

    @Query("SELECT * FROM Notes")
    fun getNotes() : List<Notes>

    @Delete
    fun deleteNotes(notes: Notes)

    @Update
    fun updateNoted(notes: Notes)

    @Query("SELECT * FROM Notes WHERE id = :id")
    fun getNotesById(id: Int) : Notes
}