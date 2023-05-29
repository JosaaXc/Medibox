package com.example.medibox

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class verPerfil : AppCompatActivity() {

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
    private var selectedObject: Any? = null

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

        val imageView = findViewById<ImageView>(R.id.imageView2)
        makeImageViewCircular(imageView)
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
        }

        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
        val resourceId = sharedPreferences.getInt("claveObjetoSeleccionado", 0) // Obtener el identificador de recurso guardado

        if (resourceId != 0) {
            imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
        }
        }

    fun makeImageViewCircular(imageView: ImageView) {
        imageView.background = resources.getDrawable(R.drawable.round_image)
        imageView.clipToOutline = true
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
                val avatar = snapshot.child("Imagen").getValue(Int::class.java)
                if (avatar != null) {
                    val avatarMostrar = diccionario[avatar]
                    val resourceId = resources.getIdentifier(avatarMostrar, "drawable", packageName)
                    val imageView = findViewById<ImageView>(R.id.imageView2)
                    imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
                }



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
    }
    fun registrarse(view: View) {
        val intent = Intent(this, registrarUsuario::class.java)
        startActivity(intent)
    }
    fun clicCancelar(view: View){
        finish()
    }
    @SuppressLint("SuspiciousIndentation")
    fun imgPerfil(view: View){
        val dialogBuilder = AlertDialog.Builder(view.context)

        // Inflar el diseño de la ventana flotante
        val inflater = LayoutInflater.from(view.context)
        val dialogView = inflater.inflate(R.layout.dialog_layout, null)

        val imageView1 = dialogView.findViewById<ImageView>(R.id.imageView1)
        val imageView2 = dialogView.findViewById<ImageView>(R.id.imageView2)
        val imageView3 = dialogView.findViewById<ImageView>(R.id.imageView3)
        val imageView4 = dialogView.findViewById<ImageView>(R.id.imageView4)
        val imageMarceline = dialogView.findViewById<ImageView>(R.id.imageView5)
        val imageNum3 = dialogView.findViewById<ImageView>(R.id.imageView6)
        val imageDPrincesa = dialogView.findViewById<ImageView>(R.id.imageView7)
        val imageRaven = dialogView.findViewById<ImageView>(R.id.imageView8)

        var alertDialog: AlertDialog? = null

        // Establecer listener de clic para el ImageView1
        imageView1.setOnClickListener {
            val resourceId = R.drawable.emo// Aquí estableces el objeto seleccionado correspondiente a imageView1
                    // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId, idDeLaImagen = 1)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }

        // Establecer listener de clic para el ImageView2
        imageView2.setOnClickListener {
            val resourceId = R.drawable.jake// Aquí estableces el objeto seleccionado correspondiente a imageView2
                    // Guardar el objeto seleccionado en SharedPreferences
                guardarObjetoSeleccionado(resourceId, idDeLaImagen = 2)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }

        imageView3.setOnClickListener {
            val resourceId = R.drawable.batman// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId, idDeLaImagen = 3)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageView4.setOnClickListener {
            val resourceId = R.drawable.robin// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId, idDeLaImagen = 4)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageMarceline.setOnClickListener {
            val resourceId = R.drawable.marceline// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId, idDeLaImagen = 5)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageNum3.setOnClickListener {
            val resourceId = R.drawable.numero3// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId, idDeLaImagen = 6)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageDPrincesa.setOnClickListener {
            val resourceId = R.drawable.dprincesa// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId, idDeLaImagen = 7)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageRaven.setOnClickListener {
            val resourceId = R.drawable.raven// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId, idDeLaImagen = 8)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }

        dialogBuilder.setView(dialogView)

        // Crear y mostrar el diálogo emergente
        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    fun guardarObjetoSeleccionado(resourceId: Int, idDeLaImagen: Int) {
        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("claveObjetoSeleccionado", resourceId)
        editor.apply()

        val boton = findViewById<TextView>(R.id.actualizar)
        boton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid.toString()
            databasereference.child("Persona").child(uid).child("Imagen").setValue(idDeLaImagen)
            Toast.makeText(getApplicationContext(), "Actualizado con éxito", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val resourceId = sharedPreferences.getInt("claveObjetoSeleccionado", 0) // Obtener el identificador de recurso guardado
        val imageView = findViewById<ImageView>(R.id.imageView2)
        if (resourceId != 0) {
            imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
        }
    }

}