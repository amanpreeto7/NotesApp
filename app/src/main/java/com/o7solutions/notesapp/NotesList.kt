package com.o7solutions.notesapp

import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.o7solutions.notesapp.databinding.FragmentNotesListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NotesList.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesList : Fragment(), NotesClickInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentNotesListBinding
    lateinit var mainActivity: MainActivity
    lateinit var adapter: NotesListAdapter
    var notesList = ArrayList<Notes>()
    lateinit var notesDB: NotesDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesListBinding.inflate(layoutInflater)
        notesDB = NotesDB.getDatabase(mainActivity)
        binding.btnAdd.setOnClickListener {
            mainActivity.navController.navigate(R.id.addNotesFragment)
        }
        adapter = NotesListAdapter(notesList, this)
        getNotes()
        binding.lvNotes.adapter = adapter
        return binding.root
    }

    fun getNotes(){
        notesList.clear()
        adapter.notifyDataSetChanged()
        class getNotesClass : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                notesList.addAll(notesDB.notesDbInterface().getNotes())
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                adapter.notifyDataSetChanged()
            }
        }
        getNotesClass().execute()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NotesList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NotesList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDeleteClick(notes: Notes) {
        AlertDialog.Builder(mainActivity)
            .setTitle(mainActivity.resources.getString(R.string.delete))
            .setMessage(mainActivity.resources.getString(R.string.delete_msg))
            .setPositiveButton(mainActivity.resources.getString(R.string.yes)){_,_->
                class delete : AsyncTask<Void, Void, Void>(){
                    override fun doInBackground(vararg params: Void?): Void? {
                        notesDB.notesDbInterface().deleteNotes(notes)
                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        getNotes()
                    }
                }
                delete().execute(

                )
            }
            .setNegativeButton(mainActivity.resources.getString(R.string.no)){_,_->

            }
            .show()

    }

    override fun onEditClick(notes: Notes) {
        var bundle = Bundle()
        bundle.putInt("id", notes.id)
        mainActivity.navController.navigate(R.id.addNotesFragment, bundle)
    }
}