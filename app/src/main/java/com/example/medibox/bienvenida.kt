package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val ocultar = findViewById<TextView>(R.id.cerrarsesion)
        ocultar.visibility = View.GONE

        if (FirebaseAuth.getInstance().currentUser != null) {
            ocultar.visibility = View.VISIBLE
            setUp()
        } else {
            ocultar.visibility = View.GONE
        }
    }

    fun setUp() {
        val cerrarsesion = findViewById<TextView>(R.id.cerrarsesion)
        cerrarsesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent)
            finish()
        }
    }
        fun abrirActividad(clase: Class<*>) {
            val intent = Intent(this, clase)
            startActivity(intent)
        }

        fun verPerfil(view: View) {
            abrirActividad(verPerfil::class.java)
        }

        fun vistaRitmoc(view: View) {
            abrirActividad(signoRitmoc::class.java)
        }

        fun vistaGlucosa(view: View) {
            abrirActividad(signoGlucosa::class.java)
        }

        fun oxigenacion(view: View) {
            abrirActividad(signoOxigenacion::class.java)
        }

        fun temperatura(view: View) {
            abrirActividad(signoTemperatura::class.java)
        }

        fun imc(view: View) {
            abrirActividad(signoImc::class.java)
        }

        fun vistaVerMisdatos(view: View) {
            abrirActividad(verDatos::class.java)
        }
}