package com.example.medibox

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import java.text.SimpleDateFormat
import java.util.*

class agregarPerfil : AppCompatActivity() {
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
    private lateinit var imageView: ImageView // Declarar la variable imageView como una propiedad de la clase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_perfil)
        datePicker()

        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        val aceptar = findViewById<TextView>(R.id.button)
        aceptar.setOnClickListener {
            editor.putString("nombre", findViewById<EditText>(R.id.nombre).text.toString())
            editor.apply()
            editor.putString("apellidoP", findViewById<EditText>(R.id.ap).text.toString())
            editor.apply()
            editor.putString("apellidoM", findViewById<EditText>(R.id.apellidom).text.toString())
            editor.apply()
            editor.putString("fecha", findViewById<EditText>(R.id.editTextDate).text.toString())
            editor.apply()

            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent)
        }
    }

    fun datePicker(){
        var selectedDate: String? = null

        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val selectedDateFormatted = dateFormat.format(selectedCalendar.time)
                selectedDate = selectedDateFormatted
                editTextDate.setText(selectedDateFormatted)
            }, year, month, dayOfMonth)

        editTextDate.setOnClickListener {
            datePickerDialog.show()
        }

        imageView = findViewById<ImageView>(R.id.imageView2) // Asignar la variable imageView como propiedad de la clase

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }
    fun bienvenida(view: View) {
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
    }
}