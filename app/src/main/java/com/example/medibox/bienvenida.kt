package com.example.medibox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class bienvenida : AppCompatActivity() {
    val diccionario: Map<Int, String> = hashMapOf(
        1 to "emo",
        2 to "jake",
        3 to "batman",
        4 to "robin",
        5 to "marceline",
        6 to "numero3",
        7 to "dprincesa",
        8 to "raven"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        if(FirebaseAuth.getInstance().currentUser!=null)
            siEstaLogeado()
        else
            recibirNombre()

        val ocultar = findViewById<TextView>(R.id.cerrarsesion)
        ocultar.visibility = View.GONE

        if (FirebaseAuth.getInstance().currentUser != null) {
            ocultar.visibility = View.VISIBLE
            setUp()
        } else {
            ocultar.visibility = View.GONE
        }

        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
        val resourceId = sharedPreferences.getInt("claveObjetoSeleccionado", 0) // Obtener el identificador de recurso guardado
        val imageView = findViewById<ImageView>(R.id.imageView6)
        makeImageViewCircular(imageView)
        if (resourceId != 0) {

            imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
        }
    }
    fun makeImageViewCircular(imageView: ImageView) {
        imageView.background = resources.getDrawable(R.drawable.round_image)
        imageView.clipToOutline = true
    }

    fun siEstaLogeado(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()
        val databaseUid = FirebaseDatabase.getInstance().getReference("/Persona/$uid")
        databaseUid.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val apellidoMostrar = snapshot.child("ApellidoPaterno").value.toString()
                val nombreMostrar = snapshot.child("Nombre").value.toString()

                val NomAp = findViewById<TextView>(R.id.NomAp)
                NomAp.text = nombreMostrar + " " + apellidoMostrar
                val avatar = snapshot.child("Imagen").getValue(Int::class.java)
                if (avatar != null) {
                    val avatarMostrar = diccionario[avatar]
                    val resourceId = resources.getIdentifier(avatarMostrar, "drawable", packageName)
                    val imageView = findViewById<ImageView>(R.id.imageView6)
                    imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message) // Manejar el error adecuadamente
            }
        })
    }
    fun recibirNombre(){
        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
        val nombreAg = sharedPreferences.getString("nombre","")
        val apellidoAg = sharedPreferences.getString("apellidoP", "")
        val NomAp = findViewById<TextView>(R.id.NomAp)
        NomAp.text = nombreAg + " " + apellidoAg
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