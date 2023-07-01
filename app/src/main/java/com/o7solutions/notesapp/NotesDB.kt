package com.o7solutions.notesapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.o7solutions.notesapp.entities.Notes
import com.o7solutions.notesapp.entities.TodoEntity

/**
 * @Author: 017
 * @Date: 27/06/23
 * @Time: 9:27 am
 */
@Database(entities = [Notes::class, TodoEntity::class], version = 1)
abstract class NotesDB  : RoomDatabase(){
    abstract fun notesDbInterface() : NotesDbInterface

   companion object{
       var notesDB : NotesDB? = null
       @Synchronized
       fun getDatabase(context: Context): NotesDB{
            if(notesDB == null){
                notesDB = Room.databaseBuilder(
                    context,
                    NotesDB::class.java,
                context.resources.getString(R.string.app_name)).build()
            }
           return notesDB!! //!! double bang - value cannot be null
       }
   }


}
