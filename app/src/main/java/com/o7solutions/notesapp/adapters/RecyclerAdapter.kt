package com.o7solutions.notesapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.o7solutions.notesapp.R
import com.o7solutions.notesapp.RecyclerClickInterface

/**
 * @Author: 017
 * @Date: 10/07/23
 * @Time: 8:57 am
 */
class RecyclerAdapter(var recyclerClickInterface: RecyclerClickInterface) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        var tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        var btnEdit = itemView.findViewById<Button>(R.id.btnEdit)
        var btnDelete = itemView.findViewById<Button>(R.id.btnDelete)
    }

    //view create
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_notes, parent, false)
        return ViewHolder(view)
    }

    //item count
    override fun getItemCount(): Int {
        return 4
    }

    //data set
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.setText("Title at ${position.toString()}")
        holder.btnDelete.setOnClickListener {
            recyclerClickInterface.recyclerClick(position)
        }
    }
}