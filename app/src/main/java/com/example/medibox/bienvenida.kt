package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)
    }
    fun iniciarSesion(view: View) {
        val intent = Intent(this, verPerfil::class.java)
        startActivity(intent)
    }
    fun vistaRitmoc(view: View) {
        val intent = Intent(this, signoRitmoc::class.java)
        startActivity(intent)
    }
    fun vistaGlucosa(view: View) {
        val intent = Intent(this, signoGlucosa::class.java)
        startActivity(intent)
    }
    fun vistaVerMisdatos(view: View) {
        val intent = Intent(this, verDatos::class.java)
        startActivity(intent)
    }
}