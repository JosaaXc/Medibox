package com.example.medibox

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class iniciarSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
    }
    fun registrarse(view: View) {
        val intent = Intent(this, registrarUsuario::class.java)
        startActivity(intent)
    }
}