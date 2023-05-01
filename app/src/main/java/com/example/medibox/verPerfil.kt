package com.example.medibox

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class verPerfil : AppCompatActivity() {

    val Firebasedatabase = FirebaseDatabase.getInstance()
    val databasereference: DatabaseReference = FirebaseDatabase.getInstance().getReference("/")
    object UserValues {
        var nombreU: String? = null
        var apU: String? = null
        var amU: String? = null
        var fechaU: String? = null
    }
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_perfil)

        datePicker()

        if(FirebaseAuth.getInstance().currentUser!=null)
            siEstaLogeado()
        else
            pasarLosDatos()


        val ocultar = findViewById<TextView>(R.id.iniciarS)
        if (FirebaseAuth.getInstance().currentUser != null)
            ocultar.visibility = View.GONE else ocultar.visibility = View.VISIBLE
        val boton = findViewById<TextView>(R.id.actualizar)
        boton.setOnClickListener{
            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid.toString()
            val nombre = findViewById<TextView>(R.id.nombre).text.toString()
            val ap = findViewById<TextView>(R.id.ap).text.toString()
            val am = findViewById<TextView>(R.id.apellidom).text.toString()
            val fecha = findViewById<TextView>(R.id.editTextDate).text.toString()
            databasereference.child("Persona").child(uid).child("Nombre").setValue(nombre)
            databasereference.child("Persona").child(uid).child("ApellidoPaterno").setValue(ap)
            databasereference.child("Persona").child(uid).child("ApellidoMaterno").setValue(am)
            databasereference.child("Persona").child(uid).child("Fecha").setValue(fecha)
            Toast.makeText(getApplicationContext(), "Actualizado con éxito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent)
            finishAffinity()
        }
        }

    fun pasarLosDatos(){
        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)

        val etNombre = findViewById<TextView>(R.id.nombre)
        etNombre.text = sharedPreferences.getString("nombre","")
        val etAp = findViewById<TextView>(R.id.ap)
        etAp.text = sharedPreferences.getString("apellidoP", "")
        val etAm = findViewById<TextView>(R.id.apellidom)
        etAm.text = sharedPreferences.getString("apellidoM", "")
        val etFecha = findViewById<TextView>(R.id.editTextDate)
        etFecha.text = sharedPreferences.getString("fecha", "")

        UserValues.nombreU = etNombre.text.toString()
        UserValues.apU = etAp.text.toString()
        UserValues.amU = etAm.text.toString()
        UserValues.fechaU = etFecha.text.toString()

    }
    fun siEstaLogeado(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()
        val databaseUid = FirebaseDatabase.getInstance().getReference("/Persona/$uid")
        databaseUid.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val etNombre = findViewById<TextView>(R.id.nombre)
                val etAp = findViewById<TextView>(R.id.ap)
                val etAm = findViewById<TextView>(R.id.apellidom)
                val etFecha = findViewById<TextView>(R.id.editTextDate)

                etAp.text = snapshot.child("ApellidoPaterno").value.toString()
                etAm.text = snapshot.child("ApellidoMaterno").value.toString()
                etFecha.text = snapshot.child("Fecha").value.toString()
                etNombre.text = snapshot.child("Nombre").value.toString()

                val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
                val actualizar = sharedPreferences.edit()
                actualizar.putString("nombre", etNombre.text.toString())
                actualizar.apply()
                actualizar.putString("apellidoP", etAp.text.toString())
                actualizar.apply()
                actualizar.putString("apellidoM", etAm.text.toString())
                actualizar.apply()
                actualizar.putString("fecha", etFecha.text.toString())
                actualizar.apply()

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message) // Manejar el error adecuadamente
            }
        })
    }

    fun datePicker(){
        var selectedDate: String? = null

        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val selectedDateFormatted = dateFormat.format(selectedCalendar.time)
                selectedDate = selectedDateFormatted
                editTextDate.setText(selectedDateFormatted)
            }, year, month, dayOfMonth)

        editTextDate.setOnClickListener {
            datePickerDialog.show()
        }

        val imageView = findViewById<ImageView>(R.id.imageView2) // Asignar la variable imageView como propiedad de la clase

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }
    fun registrarse(view: View) {
        val intent = Intent(this, registrarUsuario::class.java)
        startActivity(intent)
    }
}