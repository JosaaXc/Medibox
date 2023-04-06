package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class seleccionarPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionarperfil)
    }
    fun agrPerfil(view: View){
        //Creamos el objeto para pasar de clase
        val intent = Intent (this, agregarPerfil::class.java)
        //le indicamos que inicie la siguiente pantalla
        startActivity(intent)
        //Terminamos esta interfaz
        finish()
    }

}