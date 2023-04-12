package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class registrarUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)
        setup()
    }
    private fun setup(){
        title = "Autentication"
        val registrarse = findViewById<TextView>(R.id.acceder)
        val curp = findViewById<EditText>(R.id.curp)
        val contra = findViewById<EditText>(R.id.contra)
        val confirmarcontra = findViewById<EditText>(R.id.confirmarcontra)
        registrarse.setOnClickListener{
            if(curp.text.isNotEmpty() && contra.text.isNotEmpty() && confirmarcontra.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(curp.text.toString(),
                    contra.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this, bienvenida::class.java)
                            startActivity(intent)
                            finish()
                            Toast.makeText(getApplicationContext(), "Registrado con éxito", Toast.LENGTH_SHORT).show()
                        }else{
                            showAlert()
                        }
                }
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
    }

}