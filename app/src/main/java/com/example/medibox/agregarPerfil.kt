package com.example.medibox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

class agregarPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_perfil)

        val spinnerdia: Spinner = findViewById(R.id.spinnerDia)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.dias,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerdia.adapter = adapter
    }
}