package com.o7solutions.notesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.o7solutions.notesapp.NotesClickInterface
import com.o7solutions.notesapp.R
import com.o7solutions.notesapp.ToDoClickInterface
import com.o7solutions.notesapp.entities.Notes
import com.o7solutions.notesapp.entities.TodoEntity

/**
 * @Author: 017
 * @Date: 24/06/23
 * @Time: 10:28 am
 */
class ToDoListAdapter(var list: ArrayList<TodoEntity>,
                      var toDoClickInterface: ToDoClickInterface,

) : BaseAdapter(){
    var isEnabled : Boolean = true

    fun isEnableTextView(isEnabled: Boolean){
        this.isEnabled  = isEnabled
        notifyDataSetChanged()
    }
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return  list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0L
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_todo, parent, false)
        var title = view.findViewById<EditText>(R.id.tvTitle)
        var checkbox = view.findViewById<CheckBox>(R.id.checkbox)
        title.setText(list[position].task)
        if(isEnabled == false){
            title.isEnabled = false
        }else{
            title.isEnabled = true
        }
        title.doOnTextChanged { text, start, before, count ->
            if(text.toString().length > 0) {
                text?.let {
                    toDoClickInterface.onTextChanged(position, (text?:"").toString())

                }
            }
        }
        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            toDoClickInterface.onCheckboxClick(list[position])
        }
        return view
    }
}