package com.o7solutions.notesapp

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.o7solutions.notesapp.adapters.ToDoListAdapter
import com.o7solutions.notesapp.databinding.FragmentAddNotesBinding
import com.o7solutions.notesapp.entities.Notes
import com.o7solutions.notesapp.entities.TodoEntity
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNotesFragment : Fragment(), ToDoClickInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentAddNotesBinding
    lateinit var mainActivity: MainActivity
    lateinit var notesDB: NotesDB
    private var id = -1
    var notes = Notes()
    lateinit var toDoListAdapter : ToDoListAdapter
    var todoList = ArrayList<TodoEntity>()
    var uri : Uri?= null

    var requestResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            imagePicker.launch("image/*")
        }else{
            //alert dialog
        }
    }

    var imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()){
        it?.let{
            uri = it
            binding.imageView.setImageURI(it)
        }
    }
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
        toDoListAdapter = ToDoListAdapter(todoList, this)
        binding.lvTodo.adapter = toDoListAdapter

        binding.btnAddTodo.setOnClickListener {
            todoList.add(TodoEntity())
            toDoListAdapter.notifyDataSetChanged()
        }

        binding.imageView.setOnClickListener{
            when {
                ContextCompat.checkSelfPermission(
                    mainActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    imagePicker.launch("image/*")
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestResult.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
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
                    class updateClass : AsyncTask<Void, Void, Void>(){
                        override fun doInBackground(vararg params: Void?): Void? {
                            notesDB.notesDbInterface().updateNoted(note)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            mainActivity.navController.popBackStack()

                        }
                    }
                    updateClass().execute()
                }else{

                    var note = Notes(title= binding.etTitle.text.toString(), description = binding.etDescription.text.toString())
                    if(uri != null){
                       var btmap = MediaStore.Images.Media.getBitmap(mainActivity.contentResolver, uri)

                        note.image = encodeToBase64(btmap)
                    }
                    var notesId = -1L
                    class insertClass : AsyncTask<Void, Void, Void>(){
                        override fun doInBackground(vararg params: Void?): Void? {

                            notesId = notesDB.notesDbInterface().insertNotes(note)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                           // mainActivity.navController.popBackStack()
                            addTodo(notesId)
                        }
                    }
                    insertClass().execute()
                }
                //mainActivity.noteList.add(note)
            }
        }

        binding.tvPickDate.setOnClickListener {
            var datePicker = DatePickerDialog(mainActivity, {_,year, month, date->
                var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
                var calendar = Calendar.getInstance()
                calendar.set(year, month, date)
                var selectedDate = simpleDateFormat.format(calendar.time)
                System.out.println("in selected Date $selectedDate")
            }, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE))
            datePicker.show()
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
                if(notes.image != null){
                    binding.imageView.setImageBitmap(decodeBase64(notes.image))
                }
                getTodoList()
                toDoListAdapter.isEnableTextView(false)
            }

        }
        getEntity().execute()
    }

    fun getTodoList(){
        class todoEntity : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                todoList.addAll(notesDB.notesDbInterface().getTodoById(id))
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                toDoListAdapter.notifyDataSetChanged()
                toDoListAdapter.isEnableTextView(false)
            }

        }
        todoEntity().execute()
    }

    private fun addTodo(notesId: Long) {

        for(items in todoList) {
            items.notesId = notesId.toInt()
            class insertClass : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg params: Void?): Void? {

                    notesDB.notesDbInterface().insertTodo(items)
                    return null
                }

                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                     mainActivity.navController.popBackStack()
                }
            }
            insertClass().execute()
        }
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

    override fun onCheckboxClick(todoEntity: TodoEntity) {

    }

    override fun onTextChanged(position: Int, text: String) {
        todoList[position].task = text?:""
    }

    //use this method to convert the selected image to bitmap to save in database
    fun encodeToBase64(image: Bitmap): String? {
        var imageEncoded: String = ""
        var imageConverted = getResizedBitmap(image, 500)
        val baos = ByteArrayOutputStream()
        imageConverted?.let {
            imageConverted.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)
        }

        return imageEncoded
    }

    //use this method to convert the saved string to bitmap
    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}