package com.o7solutions.notesapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
   // var noteList = arrayListOf<Notes>()
    lateinit var bottomView : BottomNavigationView
    lateinit var notesDB: NotesDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.navController)
        bottomView = findViewById(R.id.bottomView)
        bottomView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.notesList->{
                    navController.navigate(R.id.notesList)
                }
                R.id.addNotes ->{
                    navController.navigate(R.id.recyclerFragment)
                }
            }
            return@setOnItemSelectedListener true
        }

        class AClass : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void {
                TODO("Not yet implemented")
            }
        }
    }
}