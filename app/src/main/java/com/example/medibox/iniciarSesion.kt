package com.example.medibox

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class iniciarSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)
        setup()
    }
    private fun setup(){
        title = "Acceder"
        val acceder = findViewById<TextView>(R.id.btnCancelar)
        val curp = findViewById<EditText>(R.id.curp)
        val contra = findViewById<EditText>(R.id.ap)
        acceder.setOnClickListener{
            if(curp.text.isNotEmpty() && contra.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(curp.text.toString()+"@gmail.com",
                    contra.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent = Intent(this, bienvenida::class.java)
                        startActivity(intent)
                        finishAffinity()
                        Toast.makeText(getApplicationContext(), "Accedió con éxito", Toast.LENGTH_SHORT).show()
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
    fun registrarse(view: View) {
        abrirActividad(agregarPerfil::class.java)
    }
}