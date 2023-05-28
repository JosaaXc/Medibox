package com.example.medibox

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class signoImc : AppCompatActivity() {
    private lateinit var peso: EditText
    private lateinit var altura: EditText
    private lateinit var imc: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signo_imc)

        peso = findViewById(R.id.peso)
        altura = findViewById(R.id.altura)
        imc = findViewById(R.id.imc)
        imc.isEnabled = false
        imc.isFocusable = false
        imc.isClickable = false

        peso.addTextChangedListener(imcTextWatcher)
        altura.addTextChangedListener(imcTextWatcher)
    }

    private val imcTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No se requiere ninguna acción antes de que el texto cambie
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            calcularIMC()
        }

        override fun afterTextChanged(s: Editable?) {
            // No se requiere ninguna acción después de que el texto cambie
        }
    }

    private fun calcularIMC() {
        val pesoText = peso.text.toString()
        val alturaText = altura.text.toString()

        if (pesoText.isNotEmpty() && alturaText.isNotEmpty()) {
            val peso = pesoText.toDouble()
            val altura = alturaText.toDouble()

            val imcvalor = peso / (altura * altura)
            val imc = findViewById<TextView>(R.id.imc)
            imc.text = String.format("IMC: %.2f", imcvalor)
            val enviar = findViewById<TextView>(R.id.buttonEnviar)
            enviar.setOnClickListener{
                val formattedIMC = String.format("%.2f", imcvalor)
                enviarSignos(formattedIMC.toDouble())
            }
        }
    }
    fun enviarSignos(valor: Double) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val signosReference = databaseReference.child("Signos").child(uid).child("IMC")

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val formatoDiaSemana = SimpleDateFormat("EEEE", Locale("es", "ES"))
        val currentDayOfWeek = formatoDiaSemana.format(Date()).capitalize()

        val registroReference = signosReference.child(currentDate)
        registroReference.child("valor").setValue(valor.toString())
        registroReference.child("dia").setValue(currentDayOfWeek)
        registroReference.child("hora").setValue(currentTime)

        Toast.makeText(this@signoImc, "$valor\nFue agregado con éxito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finishAffinity()
    }
}