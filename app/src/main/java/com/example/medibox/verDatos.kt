package com.example.medibox

import android.content.Intent
import com.example.medibox.databinding.ActivityVerDatosBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class verDatos : AppCompatActivity() {
    private lateinit var binding: ActivityVerDatosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerDatosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        obtenerRegistros()
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
    private fun filtrarRegistrosPorSemana(anio: Int, mes: Int, semana: Int) {
        val registrosLayout = binding.registrosLayout
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid.toString()

        val signosReference = FirebaseDatabase.getInstance().reference.child("Signos").child(uid)
        registrosLayout.removeAllViews() // Limpiar las vistas anteriores

        // Obtener los registros y aplicar el filtro por semana
        signosReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (signoSnapshot in dataSnapshot.children) {
                    val signo = signoSnapshot.key.toString()

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
                            // Crear una vista para mostrar el registro
                            val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                            val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                            val diaTextView = registroView.findViewById<TextView>(R.id.tvDia)
                            val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                            val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                            val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)

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

        // Obtener los registros y aplicar el filtro por año
        signosReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (signoSnapshot in dataSnapshot.children) {
                    val signo = signoSnapshot.key.toString()

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
                            // Crear una vista para mostrar el registro
                            val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                            val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                            val diaTextView = registroView.findViewById<TextView>(R.id.tvDia)
                            val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                            val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                            val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)

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

        // Obtener los registros y aplicar el filtro por mes
        signosReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (signoSnapshot in dataSnapshot.children) {
                    val signo = signoSnapshot.key.toString()

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
                            // Crear una vista para mostrar el registro
                            val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                            val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                            val diaTextView = registroView.findViewById<TextView>(R.id.tvDia)
                            val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                            val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                            val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)

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
                        val dia = fechaSnapshot.child("dia").getValue(String::class.java)
                        val hora = fechaSnapshot.child("hora").getValue(String::class.java)

                        // Crear una vista para mostrar el registro
                        val registroView = layoutInflater.inflate(R.layout.item_registro, null)
                        val fechaTextView = registroView.findViewById<TextView>(R.id.tvFecha)
                        val diaTextView = registroView.findViewById<TextView>(R.id.tvDia)
                        val valorTextView = registroView.findViewById<TextView>(R.id.tvValor)
                        val horaTextView = registroView.findViewById<TextView>(R.id.tvHora)
                        val signoTextView = registroView.findViewById<TextView>(R.id.tvSigno)

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
}
