package com.example.medibox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class signoImc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signo_imc)
    }
    fun imcCancelar(view: View){
        finish()
    }
}