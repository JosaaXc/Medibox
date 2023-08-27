package com.example.medibox

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class enviarDatos : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enviar_datos)

        val btnEnviarpdf = findViewById<TextView>(R.id.btnEnviarPDF)
        val emailText = findViewById<EditText>(R.id.nombreEmail)
        btnEnviarpdf.setOnClickListener {
            val correoEmail = emailText.text.toString()

            // Verifica que se haya ingresado un correo válido
            if (correoEmail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(correoEmail).matches()) {
                // Obtén la ruta del archivo PDF generado
                val fileName = "archivoMediBox.pdf"
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val mediboxDir = File(downloadsDir, "Medibox")
                val file = File(mediboxDir, fileName)

                // Verifica si el archivo PDF existe
                if (file.exists()) {
                    Thread {
                        try {
                            val properties = Properties()
                            properties.put("mail.smtp.host", "smtp.gmail.com")
                            properties.put("mail.smtp.socketFactory.port", "465")
                            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
                            properties.put("mail.smtp.auth", "true")
                            properties.put("mail.smtp.port", "465")

                            val session = Session.getDefaultInstance(properties,
                                object : javax.mail.Authenticator() {
                                    override fun getPasswordAuthentication(): PasswordAuthentication {
                                        return PasswordAuthentication("medibox.jmag@gmail.com", "nqfgmtduklxwzplw")
                                    }
                                })

                            val message = MimeMessage(session)
                            message.setFrom(InternetAddress("medibox.jmag@gmail.com"))
                            message.addRecipient(Message.RecipientType.TO, InternetAddress(correoEmail))
                            message.subject = "Envío de archivo PDF"
                            message.setText("Adjunto encontrarás el archivo PDF solicitado.")

                            val multipart = MimeMultipart()
                            val messageBodyPart = MimeBodyPart()
                            val source = FileDataSource(file)
                            messageBodyPart.dataHandler = DataHandler(source)
                            messageBodyPart.fileName = file.name
                            multipart.addBodyPart(messageBodyPart)

                            message.setContent(multipart)

                            Transport.send(message)

                            runOnUiThread {
                                Toast.makeText(this@enviarDatos, "Correo enviado exitosamente", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            runOnUiThread {
                                Toast.makeText(this@enviarDatos, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.start()
                } else {
                    Toast.makeText(this@enviarDatos, "El archivo PDF no existe", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@enviarDatos, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show()
            }
        }


    }
    fun verPDF(view: View){
        val fileName = "archivoMediBox.pdf"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val mediboxDir = File(downloadsDir, "Medibox")
        val file = File(mediboxDir, fileName)

        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        intent.setDataAndType(uri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val chooser = Intent.createChooser(intent, "Abrir archivo PDF")
        if (chooser.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(this@enviarDatos, "No se encontró una aplicación para abrir archivos PDF", Toast.LENGTH_SHORT).show()
        }
    }
    fun btnCancelarED(view: View){
        finish()
    }
}