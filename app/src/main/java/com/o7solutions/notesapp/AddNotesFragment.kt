package com.o7solutions.notesapp

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.o7solutions.notesapp.databinding.FragmentAddNotesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNotesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentAddNotesBinding
    lateinit var mainActivity: MainActivity
    lateinit var notesDB: NotesDB
    var id = -1
    var notes = Notes()
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
        binding = FragmentAddNotesBinding.inflate(layoutInflater)
        notesDB = NotesDB.getDatabase(mainActivity)

        arguments?.let {
            id = it.getInt("id")
            if(id>-1){
                getEntityInfo()
            }
        }
        binding.btnAdd.setOnClickListener {
            if(binding.etTitle.text.toString().isNullOrBlank()){
                binding.etTitle.error = mainActivity.resources.getString(R.string.enter_title)
            }else if(binding.etDescription.text.toString().isNullOrBlank()){
                binding.etDescription.error = mainActivity.resources.getString(R.string.enter_description)
            }else{
                if(id >-1){
                    var note = Notes(id= notes.id, title= binding.etTitle.text.toString(), description = binding.etDescription.text.toString())
                    class insertClass : AsyncTask<Void, Void, Void>(){
                        override fun doInBackground(vararg params: Void?): Void? {
                            notesDB.notesDbInterface().updateNoted(note)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            mainActivity.navController.popBackStack()

                        }
                    }
                    insertClass().execute()
                }else{
                    var note = Notes(title= binding.etTitle.text.toString(), description = binding.etDescription.text.toString())
                    class insertClass : AsyncTask<Void, Void, Void>(){
                        override fun doInBackground(vararg params: Void?): Void? {
                            notesDB.notesDbInterface().insertNotes(note)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            mainActivity.navController.popBackStack()

                        }
                    }
                    insertClass().execute()
                }
                //mainActivity.noteList.add(note)
            }
        }
        return binding.root
    }

    fun getEntityInfo(){
        class getEntity : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                notes = notesDB.notesDbInterface().getNotesById(id)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                binding.etTitle.setText(notes.title)
                binding.etDescription.setText(notes.description)
                binding.btnAdd.setText("Update")
            }

        }
        getEntity().execute()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddNotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddNotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}