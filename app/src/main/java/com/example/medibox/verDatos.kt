package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class verDatos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_datos)
    }
    fun vistaEnviarDatos(view: View) {
        val intent = Intent(this, enviarDatos::class.java)
        startActivity(intent)
    }
}