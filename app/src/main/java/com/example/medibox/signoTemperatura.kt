package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.medibox.databinding.ActivitySignoOxigenacionBinding
import com.example.medibox.databinding.ActivitySignoTemperaturaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signoTemperatura : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivitySignoTemperaturaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignoTemperaturaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ritmosTemperatura = resources.getStringArray(R.array.rg_temperatura)
        val adapter = ArrayAdapter(
            this, R.layout.list_item,
            ritmosTemperatura
        )
        with(binding.autoCompleteTextView){
            setAdapter(adapter)
            onItemClickListener = this@signoTemperatura
        }
    }

    fun btnCancelar (view: View){
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finishAffinity()
    }
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //aqui obtenemos el elemento seleccionado y lo guardamos en item
        val item = parent?.getItemAtPosition(position).toString()
        val enviarSignoss = findViewById<TextView>(R.id.guardar)
        enviarSignoss.setOnClickListener{
            enviarSignos(item)
        }
    }
    fun enviarSignos(item: String){
        val databasereference: DatabaseReference = FirebaseDatabase.getInstance().getReference("/")
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        databasereference.child("Signos").child(uid).child("temperatura").push().setValue(item)
        Toast.makeText(this@signoTemperatura, item+" \nFue agregado con éxito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finishAffinity()
    }
}