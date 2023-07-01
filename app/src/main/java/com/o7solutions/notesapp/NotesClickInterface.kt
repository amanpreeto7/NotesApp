package com.o7solutions.notesapp

import com.o7solutions.notesapp.entities.Notes
import com.o7solutions.notesapp.entities.TodoEntity

/**
 * @Author: 017
 * @Date: 29/06/23
 * @Time: 8:53 am
 */
interface NotesClickInterface {
    fun onDeleteClick(notes: Notes)
    fun onEditClick(notes: Notes)
}

interface ToDoClickInterface {
    fun onCheckboxClick(todoEntity: TodoEntity)
    fun onTextChanged(position: Int, text: String)
}