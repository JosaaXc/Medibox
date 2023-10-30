package com.example.medibox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class registrarUsuario : AppCompatActivity() {
    //declaramos variables globlaes de los botones
    private lateinit var btnRegistrarse: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)
        setup()
    }

    private fun mandarDatosBD(){
        val databasereference: DatabaseReference = FirebaseDatabase.getInstance().getReference("/")
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        //val nombre = verPerfil.UserValues.nombreU
        //val ap = verPerfil.UserValues.apU
        //val am = verPerfil.UserValues.amU
        //val fecha = verPerfil.UserValues.fechaU

        // Obtener datos de preferencias compartidas
        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
        val nombre = sharedPreferences.getString("nombre", "") // Obtén el nombre guardado
        val ap = sharedPreferences.getString("apellidoP", "") // Obtén el apellido paterno guardado
        val am = sharedPreferences.getString("apellidoM", "") // Obtén el apellido materno guardado
        val fecha = sharedPreferences.getString("fecha", "") // Obtén la fecha guardada


        databasereference.child("Persona").child(uid).child("Nombre").setValue(nombre)
        databasereference.child("Persona").child(uid).child("ApellidoPaterno").setValue(ap)
        databasereference.child("Persona").child(uid).child("ApellidoMaterno").setValue(am)
        databasereference.child("Persona").child(uid).child("Fecha").setValue(fecha)
        Toast.makeText(getApplicationContext(), "Actualizado con éxito", Toast.LENGTH_SHORT).show()
    }
    private fun setup(){
        title = "Autentication"
        btnRegistrarse = findViewById(R.id.btnRegistrar)
        val curp = findViewById<EditText>(R.id.curp)
        val contra = findViewById<EditText>(R.id.ap)
        val confirmarcontra = findViewById<EditText>(R.id.confirmarcontra)
        // quitamos los espacios en blanco
        val input = curp.text.toString().trim()
        btnRegistrarse.setOnClickListener{
            if(curp.text.isNotEmpty() && contra.text.isNotEmpty() && confirmarcontra.text.isNotEmpty()){
                if (curp.text.length == 18 && curp.text.toString().toUpperCase() == curp.text.toString()) {
                    // El campo tiene 18 caracteres y todos son mayúsculas
                    // Aquí puedes realizar las acciones que desees cuando el campo cumple las condiciones
                    if(contra.text.toString()==confirmarcontra.text.toString() && contra.text.toString().length>=6) {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            curp.text.toString() + "@gmail.com",
                            contra.text.toString()
                        ).addOnCompleteListener {
                            if (it.isSuccessful) {
                                mandarDatosBD()
                                val intent = Intent(this, bienvenida::class.java)
                                startActivity(intent)
                                finish()
                                Toast.makeText(
                                    getApplicationContext(),
                                    "Registrado con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showAlert()
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "La contraseña debe ser igual y mayor a 6 digitos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // El campo no cumple con las condiciones de 18 caracteres y en mayuscula
                    Toast.makeText(this@registrarUsuario, "La curp debe tener exactamente 18 caracteres en mayúscula y sin espacios.", Toast.LENGTH_LONG).show()
                    Toast.makeText(this@registrarUsuario, "Ingresado:" + curp.text, Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@registrarUsuario, "valor del input: " + input, Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(getApplicationContext(), "Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error en la autenticar al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    fun abrirActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
    fun iniciarSesion(view: View) {
        abrirActividad(iniciarSesion::class.java)
        finishAffinity()
    }

}