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
    fun abrirActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
    fun iniciarSesion(view: View) {
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
    }fun imc(view: View) {
        abrirActividad(signoImc::class.java)
    }
    fun vistaVerMisdatos(view: View) {
        abrirActividad(verDatos::class.java)
    }
}