package com.o7solutions.notesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * @Author: 017
 * @Date: 24/06/23
 * @Time: 10:28 am
 */
class NotesListAdapter(var list: ArrayList<Notes>) : BaseAdapter(){
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
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.item_notes, parent, false)
        var title = view.findViewById<TextView>(R.id.tvTitle)
        var description = view.findViewById<TextView>(R.id.tvDescription)
        title.setText(list[position].title)
        description.setText(list[position].description)
        return view
    }
}