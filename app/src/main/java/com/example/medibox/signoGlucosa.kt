package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.medibox.databinding.ActivitySignoGlucosaBinding
import com.example.medibox.databinding.ActivitySignoRitmocBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this@signoGlucosa, item, Toast.LENGTH_SHORT).show()
        val enviarSignoss = findViewById<TextView>(R.id.guardar)
        enviarSignoss.setOnClickListener{
            enviarSignos(item)
        }
    }
    fun enviarSignos(item: String){
        val databasereference: DatabaseReference = FirebaseDatabase.getInstance().getReference("/")
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        databasereference.child("Signos").child(uid).child("glucosa").push().setValue(item)
        Toast.makeText(this@signoGlucosa, item+" \nFue agregado con éxito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
        finishAffinity()
    }
}