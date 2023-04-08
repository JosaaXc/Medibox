package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner

class signoGlucosa : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signo_glucosa)
        //leemos las opciones del array ubicado den strings.xml
        val opciones = resources.getStringArray(R.array.rg_glucosa)
        //Creamos el adapatador
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        val spinner = findViewById <Spinner> (R.id.mi_spinner)
        spinner.adapter = adaptador
    }
    fun btnCancelar (view: View){
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finish()
    }
}