package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.medibox.databinding.ActivitySignoRitmocBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signoRitmoc : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var binding: ActivitySignoRitmocBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignoRitmocBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ritmosC = resources.getStringArray(R.array.rg_ritmoc)
        val adapter = ArrayAdapter(
            this, R.layout.list_item,
            ritmosC
        )
        with(binding.autoCompleteTextView){
            setAdapter(adapter)
            onItemClickListener = this@signoRitmoc
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

        databasereference.child("Signos").child(uid).child("ritmoCardiaco").push().setValue(item)
        Toast.makeText(this@signoRitmoc, item+"Fue agregado con éxito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finishAffinity()
    }
}