package com.example.medibox

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import java.util.*

class agregarPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_perfil)

        var selectedDate: String? = null

        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                selectedDate = "$year-${month + 1}-$dayOfMonth"
                editTextDate.setText(selectedDate)
            }, year, month, dayOfMonth)

        editTextDate.setOnClickListener {
            datePickerDialog.show()
        }


    }
}