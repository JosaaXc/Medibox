package com.example.medibox

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class agregarPerfil : AppCompatActivity() {
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
    private lateinit var imageView: ImageView // Declarar la variable imageView como una propiedad de la clase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_perfil)
        datePicker()

        val iniciarS = findViewById<TextView>(R.id.iniciarS)
        iniciarS.setOnClickListener{
            val intent = Intent(this, iniciarSesion::class.java)
            startActivity(intent)

        }

        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        val aceptar = findViewById<TextView>(R.id.button)
        aceptar.setOnClickListener {

            editor.putString("nombre", findViewById<EditText>(R.id.nombre).text.toString())
            editor.apply()
            editor.putString("apellidoP", findViewById<EditText>(R.id.ap).text.toString())
            editor.apply()
            editor.putString("apellidoM", findViewById<EditText>(R.id.apellidom).text.toString())
            editor.apply()
            editor.putString("fecha", findViewById<EditText>(R.id.editTextDate).text.toString())
            editor.apply()

            val intent = Intent(this, bienvenida::class.java)
            startActivity(intent)
        }
        val imageView = findViewById<ImageView>(R.id.imageView2)
        makeImageViewCircular(imageView)
    }
    fun makeImageViewCircular(imageView: ImageView) {
        imageView.background = resources.getDrawable(R.drawable.round_image)
        imageView.clipToOutline = true
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }
    fun bienvenida(view: View) {
        val intent = Intent(this, bienvenida::class.java)
        startActivity(intent)
    }
    fun imgCrearPerfil(view: View){
        val dialogBuilder = AlertDialog.Builder(view.context)

        // Inflar el diseño de la ventana flotante
        val inflater = LayoutInflater.from(view.context)
        val dialogView = inflater.inflate(R.layout.dialog_layout, null)

        val imageView1 = dialogView.findViewById<ImageView>(R.id.imageView4)
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
            guardarObjetoSeleccionado(resourceId)
            alertDialog?.dismiss() // Cierra el diálogo emergente
            Toast.makeText(getApplicationContext(), "Imagen de FINN", Toast.LENGTH_SHORT).show()
        }

        // Establecer listener de clic para el ImageView2
        imageView2.setOnClickListener {
            val resourceId = R.drawable.jake// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }

        imageView3.setOnClickListener {
            val resourceId = R.drawable.batman// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageView4.setOnClickListener {
            val resourceId = R.drawable.robin// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageMarceline.setOnClickListener {
            val resourceId = R.drawable.marceline// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageNum3.setOnClickListener {
            val resourceId = R.drawable.numero3// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageDPrincesa.setOnClickListener {
            val resourceId = R.drawable.dprincesa// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }
        imageRaven.setOnClickListener {
            val resourceId = R.drawable.raven// Aquí estableces el objeto seleccionado correspondiente a imageView2
            // Guardar el objeto seleccionado en SharedPreferences
            guardarObjetoSeleccionado(resourceId)
            alertDialog?.dismiss() // Cierra el diálogo emergente
        }

        dialogBuilder.setView(dialogView)

        // Crear y mostrar el diálogo emergente
        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
    fun guardarObjetoSeleccionado(resourceId: Int) {
        val sharedPreferences = getSharedPreferences("DatosPersona", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("claveObjetoSeleccionado", resourceId)
        editor.apply()


        val resourceId = sharedPreferences.getInt("claveObjetoSeleccionado", 0) // Obtener el identificador de recurso guardado
        val imageView = findViewById<ImageView>(R.id.imageView2)
        if (resourceId != 0) {
            imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
        }

    }
}