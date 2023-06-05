package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.medibox.databinding.ActivitySignoGlucosaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class signoGlucosa : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivitySignoGlucosaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignoGlucosaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ritmosGlucosa = resources.getStringArray(R.array.rg_glucosa)
        val adapter = ArrayAdapter(
            this, R.layout.list_item,
            ritmosGlucosa
        )
        with(binding.autoCompleteTextView){
            setAdapter(adapter)
            onItemClickListener = this@signoGlucosa
        }
    }
    fun btnCancelar (view: View){
        finish()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this@signoGlucosa, item, Toast.LENGTH_SHORT).show()
        val enviarSignoss = findViewById<TextView>(R.id.guardar)
        enviarSignoss.setOnClickListener{
            enviarSignos(item)
        }
    }
    fun enviarSignos(valor: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val signosReference = databaseReference.child("Signos").child(uid).child("Glucosa")

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val formatoDiaSemana = SimpleDateFormat("EEEE", Locale("es", "ES"))
        val currentDayOfWeek = formatoDiaSemana.format(Date()).capitalize()


        val registroReference = signosReference.child(currentDate)
        registroReference.child("valor").setValue(valor)
        registroReference.child("dia").setValue(currentDayOfWeek)
        registroReference.child("hora").setValue(currentTime)

        Toast.makeText(this@signoGlucosa, "$valor\nFue agregado con éxito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finishAffinity()
    }
}