package com.example.medibox

import android.content.Intent
import com.example.medibox.databinding.ActivityVerDatosBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class verDatos : AppCompatActivity() {
    private lateinit var binding: ActivityVerDatosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerDatosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        obtenerRegistros()
    }

    fun enviarDatos(view: View) {
        val intent = Intent(this, enviarDatos::class.java)
        startActivity(intent)
    }

    private fun obtenerRegistros() {
        val registrosLayout = binding.registrosLayout
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        val signosReference = FirebaseDatabase.getInstance().reference.child("Signos").child(uid)

        signosReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (signoSnapshot in dataSnapshot.children) {
                    val signo = signoSnapshot.key.toString()

                    for (fechaSnapshot in signoSnapshot.children) {
                        val fecha = fechaSnapshot.key.toString()
                        val valor = fechaSnapshot.child("valor").getValue(String::class.java)
                        val hora = fechaSnapshot.child("hora").getValue(String::class.java)

                        // Crear una vista para mostrar el registro
                        val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                        val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                        val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                        val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                        val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)

                        signoTextView.text = signo
                        fechaTextView.text = fecha
                        valorTextView.text = valor
                        horaTextView.text = hora

                        // Agregar la vista del registro al diseño principal
                        registrosLayout.addView(registroView)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejo de errores, si es necesario
            }
        })
    }
}
