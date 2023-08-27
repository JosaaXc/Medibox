package com.example.medibox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.medibox.databinding.ActivityVerDatosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.util.*
//librerias para los permisos de escritura en almacenamiento:
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileNotFoundException

class verDatos : AppCompatActivity() {
    val registros: HashMap<String, String> = hashMapOf(
        "Glucosa" to "icon_glucosa",
        "IMC" to "icon_imc",
        "Oxigenación" to "icon_oxigenacion",
        "Ritmo Cardiaco" to "ritmo_cardiaco",
        "Temperatura" to "icon_temperatura",
        // Agrega más elementos aquí si es necesario
    )
    private val listaRegistros: MutableList<Registro> = mutableListOf()
    data class Registro(
        val signo: String,
        val fecha: String,
        val valor: String,
        val dia: String,
        val hora: String
    )

    private lateinit var binding: ActivityVerDatosBinding

    private val STORAGE_PERMISSION_CODE = 1 // Puedes elegir cualquier número como identificador de solicitud
    private val STORAGE_PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerDatosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        obtenerRegistros()

        //clase para generar pdf:
        val enviarPDF = findViewById<Button>(R.id.btnEnviarDatos)
        enviarPDF.setOnClickListener{
            generarPDF()
        }
        // Verificar si los permisos de almacenamiento están otorgados
        val permissionsToRequest = mutableListOf<String>()
        for (permission in STORAGE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            // Si algún permiso no está otorgado, solicitarlos
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), STORAGE_PERMISSION_CODE)
        } else {
            // Todos los permisos de almacenamiento otorgados
            // Puedes realizar operaciones de lectura y escritura aquí
            Toast.makeText(this, "Permisos aceptados", Toast.LENGTH_SHORT).show()
        }

        val btnSeleccionarAnio = findViewById<Button>(R.id.btnSeleccionarAnio)
        val btnSeleccionarMes = findViewById<Button>(R.id.btnSeleccionarMes)
        val btnSeleccionarSemana = findViewById<Button>(R.id.btnSeleccionarSemana)
        val fecha = findViewById<TextView>(R.id.mostraFecha)

        var añoSeleccionado = Calendar.getInstance().get(Calendar.YEAR)
        var mesSeleccionado = Calendar.getInstance().get(Calendar.MONTH) + 1
        // Establecer el valor predeterminado de añoSeleccionado al año actual
        fecha.text = "Año: " + añoSeleccionado.toString()

        btnSeleccionarAnio.setOnClickListener {
                    val añosDisponibles = listOf(2020, 2021, 2022, 2023, 2024) // Lista de años disponibles

                    // Crear el NumberPicker con los años disponibles
                    val numberPicker = NumberPicker(this)
                    numberPicker.minValue = añosDisponibles.first()
                    numberPicker.maxValue = añosDisponibles.last()
                    numberPicker.wrapSelectorWheel = false
                    numberPicker.displayedValues = añosDisponibles.map { it.toString() }.toTypedArray()

                    // Crear el cuadro de diálogo
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Seleccionar año")
                    alertDialogBuilder.setView(numberPicker)

                    // Configurar los botones del cuadro de diálogo
                    alertDialogBuilder.setPositiveButton("Aceptar") { _, _ ->
                        // Obtener el año seleccionado
                        añoSeleccionado = numberPicker.value

                        // Actualizar el texto en el TextView
                        fecha.text = "Año: $añoSeleccionado"

                        // Aplicar el filtro y mostrar solo los registros que coincidan con el año seleccionado
                        filtrarRegistrosPorAnio(añoSeleccionado)
                    }
                    alertDialogBuilder.setNegativeButton("Cancelar", null)

                    // Mostrar el cuadro de diálogo
                    alertDialogBuilder.create().show()
                }

        btnSeleccionarMes.setOnClickListener {
            // Lista de meses disponibles
            val mesesDisponibles = arrayOf(
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            )

            // Crear el NumberPicker con los meses disponibles
            val numberPicker = NumberPicker(this)
            numberPicker.minValue = 1
            numberPicker.maxValue = mesesDisponibles.size
            numberPicker.displayedValues = mesesDisponibles

            // Crear el cuadro de diálogo
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Seleccionar mes")
            alertDialogBuilder.setView(numberPicker)

            // Configurar los botones del cuadro de diálogo
            alertDialogBuilder.setPositiveButton("Aceptar") { _, _ ->
                // Obtener el mes seleccionado
                mesSeleccionado = numberPicker.value

                // Actualizar el texto en el TextView
                fecha.text = "Año: $añoSeleccionado  Mes: $mesSeleccionado"

                // Aplicar el filtro y mostrar solo los registros que coincidan con el mes seleccionado
                filtrarRegistrosPorMes(añoSeleccionado, mesSeleccionado)
            }
            alertDialogBuilder.setNegativeButton("Cancelar", null)

            // Mostrar el cuadro de diálogo
            alertDialogBuilder.create().show()
        }

        btnSeleccionarSemana.setOnClickListener {
            // Lista de semanas disponibles con sus rangos de días
            val semanasDisponibles = arrayOf(
                "Semana 1: 1 al 7",
                "Semana 2: 8 al 14",
                "Semana 3: 15 al 21",
                "Últimos dias del mes: 22 al 31"
            )

            // Crear el NumberPicker con las semanas disponibles
            val numberPicker = NumberPicker(this)
            numberPicker.minValue = 1
            numberPicker.maxValue = semanasDisponibles.size
            numberPicker.displayedValues = semanasDisponibles

            // Crear el cuadro de diálogo
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Seleccionar semana")
            alertDialogBuilder.setView(numberPicker)

            // Configurar los botones del cuadro de diálogo
            alertDialogBuilder.setPositiveButton("Aceptar") { _, _ ->
                // Obtener la semana seleccionada
                val semanaSeleccionada = numberPicker.value

                // Actualizar el texto en el TextView
                fecha.text = "Año: $añoSeleccionado  Mes: $mesSeleccionado Semana: $semanaSeleccionada"

                // Aplicar el filtro y mostrar solo los registros que coincidan con la semana seleccionada
                filtrarRegistrosPorSemana(añoSeleccionado, mesSeleccionado, semanaSeleccionada)
            }
            alertDialogBuilder.setNegativeButton("Cancelar", null)

            // Mostrar el cuadro de diálogo
            alertDialogBuilder.create().show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            var allPermissionsGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }

            if (allPermissionsGranted) {
                // Todos los permisos otorgados
                // Puedes realizar operaciones de lectura y escritura aquí
                Toast.makeText(this, "permisos ya aceptados", Toast.LENGTH_SHORT).show()
            } else {
                // Alguno de los permisos fue denegado
                // Manejar esta situación si es necesario
            }
        }
    }

    private fun filtrarRegistrosPorSemana(anio: Int, mes: Int, semana: Int) {
        val registrosLayout = binding.registrosLayout
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        val signosReference = FirebaseDatabase.getInstance().reference.child("Signos").child(uid)
        registrosLayout.removeAllViews() // Limpiar las vistas anteriores
        listaRegistros.clear()

        // Obtener los registros y aplicar el filtro por semana
        signosReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (signoSnapshot in dataSnapshot.children) {
                    val signo = signoSnapshot.key.toString()
                    val signoImagen = registros[signo]

                    for (fechaSnapshot in signoSnapshot.children) {
                        val fecha = fechaSnapshot.key.toString()
                        val valor = fechaSnapshot.child("valor").getValue(String::class.java)
                        val dia = fechaSnapshot.child("dia").getValue(String::class.java)
                        val hora = fechaSnapshot.child("hora").getValue(String::class.java)

                        // Obtener el número de semana de la fecha
                        val fechaSplit = fecha.split("-")
                        val anioActual = fechaSplit[2].toInt()
                        val mesActual = fechaSplit[1].toInt()
                        val diaMes = fechaSplit[0].toInt()
                        val numeroSemana = obtenerNumeroSemana(diaMes)

                        // Aplicar el filtro
                        if (anioActual == anio && mesActual == mes && numeroSemana == semana) {

                            val valorNonNull = valor ?: ""
                            val diaNonNull = dia ?: ""
                            val horaNonNull = hora ?: ""
                            val registro = Registro(signo, fecha, valorNonNull, diaNonNull, horaNonNull)
                            listaRegistros.add(registro)
                            // Crear una vista para mostrar el registro
                            val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                            val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                            val diaTextView = registroView.findViewById<TextView>(R.id.tvDia)
                            val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                            val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                            val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)
                            if (signoImagen != null) {
                                val resourceId = resources.getIdentifier(signoImagen, "drawable", packageName)
                                val imageView = registroView.findViewById<ImageView>(R.id.ivIcon)
                                imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
                            }

                            signoTextView.text = signo
                            diaTextView.text = dia
                            fechaTextView.text = fecha
                            valorTextView.text = valor
                            horaTextView.text = hora


                            // Agregar la vista del registro al diseño principal
                            registrosLayout.addView(registroView)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de base de datos
            }
        })
    }

    // Función para obtener el número de semana a partir de un día del mes
    private fun obtenerNumeroSemana(dia: Int): Int {
        if(dia >= 28)
            return 4
        else
            return (dia - 1) / 7 + 1
    }

    private fun filtrarRegistrosPorAnio(anio: Int) {
        val registrosLayout = binding.registrosLayout
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        val signosReference = FirebaseDatabase.getInstance().reference.child("Signos").child(uid)
        registrosLayout.removeAllViews() // Limpiar las vistas anteriores
        listaRegistros.clear()

        // Obtener los registros y aplicar el filtro por año
        signosReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (signoSnapshot in dataSnapshot.children) {
                    val signo = signoSnapshot.key.toString()
                    val signoImagen = registros[signo]

                    for (fechaSnapshot in signoSnapshot.children) {
                        val fecha = fechaSnapshot.key.toString()
                        val valor = fechaSnapshot.child("valor").getValue(String::class.java)
                        val dia = fechaSnapshot.child("dia").getValue(String::class.java)
                        val hora = fechaSnapshot.child("hora").getValue(String::class.java)

                        // Obtener el año de la fecha actual
                        val fechaSplit = fecha.split("-")
                        val añoActual = fechaSplit[2].toInt()

                        // Aplicar el filtro
                        if (añoActual == anio) {

                            val valorNonNull = valor ?: ""
                            val diaNonNull = dia ?: ""
                            val horaNonNull = hora ?: ""
                            val registro = Registro(signo, fecha, valorNonNull, diaNonNull, horaNonNull)
                            listaRegistros.add(registro)
                            // Crear una vista para mostrar el registro
                            val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                            val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                            val diaTextView = registroView.findViewById<TextView>(R.id.tvDia)
                            val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                            val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                            val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)
                            if (signoImagen != null) {
                                val resourceId = resources.getIdentifier(signoImagen, "drawable", packageName)
                                val imageView = registroView.findViewById<ImageView>(R.id.ivIcon)
                                imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
                            }

                            signoTextView.text = signo
                            diaTextView.text = dia
                            fechaTextView.text = fecha
                            valorTextView.text = valor
                            horaTextView.text = hora

                            // Agregar la vista del registro al diseño principal
                            registrosLayout.addView(registroView)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            // ...
        })
    }

    private fun filtrarRegistrosPorMes(anio: Int, mes: Int) {
        val registrosLayout = binding.registrosLayout
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        val signosReference = FirebaseDatabase.getInstance().reference.child("Signos").child(uid)
        registrosLayout.removeAllViews() // Limpiar las vistas anteriores

        listaRegistros.clear()
        // Obtener los registros y aplicar el filtro por mes
        signosReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (signoSnapshot in dataSnapshot.children) {
                    val signo = signoSnapshot.key.toString()
                    val signoImagen = registros[signo]

                    for (fechaSnapshot in signoSnapshot.children) {
                        val fecha = fechaSnapshot.key.toString()
                        val valor = fechaSnapshot.child("valor").getValue(String::class.java)
                        val dia = fechaSnapshot.child("dia").getValue(String::class.java)
                        val hora = fechaSnapshot.child("hora").getValue(String::class.java)

                        // Obtener el mes de la fecha actual
                        val fechaSplit = fecha.split("-")
                        val mesActual = fechaSplit[1].toInt()
                        val anioActual = fechaSplit[2].toInt()

                        // Aplicar el filtro
                        if (anio == anioActual && mesActual == mes) {
                            val valorNonNull = valor ?: ""
                            val diaNonNull = dia ?: ""
                            val horaNonNull = hora ?: ""
                            val registro = Registro(signo, fecha, valorNonNull, diaNonNull, horaNonNull)
                            listaRegistros.add(registro)
                            // Crear una vista para mostrar el registro
                            val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                            val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                            val diaTextView = registroView.findViewById<TextView>(R.id.tvDia)
                            val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                            val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                            val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)
                            if (signoImagen != null) {
                                val resourceId = resources.getIdentifier(signoImagen, "drawable", packageName)
                                val imageView = registroView.findViewById<ImageView>(R.id.ivIcon)
                                imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
                            }

                            signoTextView.text = signo
                            diaTextView.text = dia
                            fechaTextView.text = fecha
                            valorTextView.text = valor
                            horaTextView.text = hora

                            // Agregar la vista del registro al diseño principal
                            registrosLayout.addView(registroView)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            // ...
        })
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
                    val signoImagen = registros[signo]
                    for (fechaSnapshot in signoSnapshot.children) {
                        val fecha = fechaSnapshot.key.toString()
                        val valor = fechaSnapshot.child("valor").getValue(String::class.java)
                        val dia = fechaSnapshot.child("dia").getValue(String::class.java)
                        val hora = fechaSnapshot.child("hora").getValue(String::class.java)

                        val valorNonNull = valor ?: ""
                        val diaNonNull = dia ?: ""
                        val horaNonNull = hora ?: ""
                        val registro = Registro(signo, fecha, valorNonNull, diaNonNull, horaNonNull)
                        listaRegistros.add(registro)

                        // Crear una vista para mostrar el registro
                        val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                        val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                        val diaTextView = registroView.findViewById<TextView>(R.id.tvDia)
                        val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                        val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                        val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)
                        if (signoImagen != null) {
                            val resourceId = resources.getIdentifier(signoImagen, "drawable", packageName)
                            val imageView = registroView.findViewById<ImageView>(R.id.ivIcon)
                            imageView.setImageResource(resourceId) // Establecer el recurso de imagen en el ImageView
                        }
                        signoTextView.text = signo
                        diaTextView.text = dia
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

    private fun generarPDF() {
        var apPaterno = ""
        var apMaterno = ""
        var nombreP = ""
        var nombreC=""
        var datosObtenidos = false
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        val curp = email?.replace("@gmail.com", "")
        //traemos el nombre
        val uid = user?.uid.toString()
        val databaseUid = FirebaseDatabase.getInstance().getReference("/Persona/$uid")
        databaseUid.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                apPaterno = snapshot.child("ApellidoPaterno").value.toString()
                apMaterno = snapshot.child("ApellidoMaterno").value.toString()
                nombreP = snapshot.child("Nombre").value.toString()

                nombreC = "$nombreP $apPaterno $apMaterno"
                datosObtenidos = true // Indicar que los datos se han obtenido correctamente

                // Aquí puedes llamar al método para generar el PDF una vez que los datos estén disponibles
                generarPDFConDatos(nombreC, curp)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message) // Manejar el error adecuadamente
            }
        })
        // Mostrar Toast y generar PDF si los datos se han obtenido correctamente
        if (datosObtenidos) {

            generarPDFConDatos(nombreC, curp)
        }
    }
    private fun generarPDFConDatos(nombreC: String, curp: String?) {
        // Resto del código para generar el PDF
        if (listaRegistros.isNotEmpty()) {
            // Crear documento PDF
            val document = Document()

            val fileName = "archivoMediBox.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val mediboxDir = File(downloadsDir, "Medibox")

            if (!mediboxDir.exists()) {
                mediboxDir.mkdirs()
            }

            val file = File(mediboxDir, fileName)

            try {
                val fileOutputStream = FileOutputStream(file)
                PdfWriter.getInstance(document, fileOutputStream)

                // Abrir el documento PDF
                document.open()

                // Resto de tu código para agregar contenido al PDF

                // Crear un párrafo para el título
                val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD)
                val titleParagraph = Paragraph("Registros de: $nombreC CURP:$curp " , titleFont)

                titleParagraph.alignment = Element.ALIGN_CENTER
                document.add(titleParagraph)

                // Crear una tabla para los registros
                val table = PdfPTable(5) // 5 columnas para cada dato del registro
                table.widthPercentage = 100f
                table.spacingBefore = 20f
                table.spacingAfter = 20f

                // Agregar encabezados de columna
                table.addCell(createCell("Signo", true))
                table.addCell(createCell("Fecha", true))
                table.addCell(createCell("Valor", true))
                table.addCell(createCell("Día", true))
                table.addCell(createCell("Hora", true))

                // Agregar registros a la tabla
                for (registro in listaRegistros) {
                    table.addCell(createCell(registro.signo))
                    table.addCell(createCell(registro.fecha))
                    table.addCell(createCell(registro.valor))
                    table.addCell(createCell(registro.dia))
                    table.addCell(createCell(registro.hora))
                }

                // Agregar la tabla al documento
                document.add(table)
                // Cerrar el documento PDF
                document.close()

                // Mostrar mensaje de éxito
                Toast.makeText(this, "PDF generado correctamente", Toast.LENGTH_SHORT).show()

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                // Manejar el error en caso de que ocurra una excepción al abrir el archivo
                Toast.makeText(this, "Error al generar el PDF", Toast.LENGTH_SHORT).show()
            }
        } else {
            // La lista de registros está vacía, no se pueden generar PDF
            Toast.makeText(this, "No hay registros disponibles", Toast.LENGTH_SHORT).show()
        }
        //nos dirijimos a la siguiente interfaz:
        val intent = Intent(this@verDatos, enviarDatos::class.java)
        startActivity(intent)
    }
    private fun createCell(content: String, isHeader: Boolean = false): PdfPCell {
        val font = if (isHeader) Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD) else Font(Font.FontFamily.HELVETICA, 12f)
        val cell = PdfPCell(Paragraph(content, font))
        cell.paddingLeft = 5f
        return cell
    }
}
