package com.example.almacenamiento

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkExternalMedia()
        // TODO (1) Obtener acceso a Shared Preference
        // TODO (2) Obtener Shared Preference desde la actividad
        // TODO (3) Especificar el nombre del archivo de preferencia (si se esta usando mas de uno)
        // TODO (3.1) Cuando se necesita solamente un archivo de preferencia en el app llamar a getPreferences con un paramentro
        // TODO (4) Asignar que la preferencias se leeran o escribiran en modo privado (Para mantender privado las configuración)
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)


        bt_save.setOnClickListener {
            // TODO (5) Para escribir valores en shared preference
            with(sharedPref.edit()) {
                putString(getString(R.string.save_email_key), et_option.text.toString()) // TODO (6) Se guardan en formato clave valor
                commit() // TODO (7) Confirma que se guarden todos los elementos añadidos

            }

            tv_data.text = et_option.text.toString() // Solamente para mostrar el valor de inmediato
        }


        // TODO (8) Para leer basta con ejecutar el metodo getXXXX y definir el valor por defecto si no existe
        val email = sharedPref.getString(getString(R.string.save_email_key), "")

        tv_data.text = email


        bt_write_internal.setOnClickListener {
            // TODO (9) openFileOutput crea un archivo y un InputStream para escribir en el
            val filename = "email.txt"
            val fileContent = "email: $email"

            // TODO (10) Al usar use obtenemos el fileInput que devuelve FileOutputStream
            // TODO (11) use cierra el FileOutputStream y maneja la exception a nivel de bloque

            openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(fileContent.toByteArray())
            }

        }


        bt_read_internal.setOnClickListener{
            // TODO (12) Abrir un archivo existente
            val filename = "email.txt"
            openFileInput(filename).use {
                val text = it.bufferedReader().readText() // TODO (13) Se lee todo el contenido
                tv_data.text = text
            }
        }

//        fun isExternalStorageWritable(): Boolean {
//            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
//        }
//
//        fun getPublicTextStorageDir(textSave: String): File? {
//            // Get the directory for the user's public pictures directory.
//            val file = File(
//                Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOWNLOADS), textSave)
//            if (!file?.mkdirs()) {
//                Log.e("TAG: ", "Directory not created")
//            }
//            return file
//        }

        bt_write_external.setOnClickListener{
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()){
                writeToSDFile(email)
            }
        }
    }

     private fun checkExternalMedia() {
        var mExternalStorageAvailable: Boolean
        var mExternalStorageWriteable: Boolean
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED == state) {
         // Can read and write the media
            mExternalStorageWriteable = true
            mExternalStorageAvailable = mExternalStorageWriteable
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
         // Can only read the media
            mExternalStorageAvailable = true
            mExternalStorageWriteable = false
        }
        else {
         // Can't read or write
            mExternalStorageWriteable = false
            mExternalStorageAvailable = mExternalStorageWriteable
        }
        Toast.makeText(this, "\n\nExternal Media: readable=" + mExternalStorageAvailable + " writable=" + mExternalStorageWriteable,
        Toast.LENGTH_LONG)
         Log.d("A VER", "\n\nExternal Media: readable=" + mExternalStorageAvailable + " writable=" + mExternalStorageWriteable)
     }


    private fun writeToSDFile(email: String) {

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        val root = android.os.Environment.getExternalStorageDirectory()
        Log.d("ROOT","\nExternal file system root: $root")

        val dir = File(root.absolutePath + "/Download")
        dir.mkdirs()
        val file = File(dir, "myData.txt")

        try {
            val f = FileOutputStream(file)
            val pw = PrintWriter(f)
            pw.println(email)
            pw.flush()
            pw.close()
            f.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.i("NO SE ENCUENTRA: ",
                "******* File not found. Did you" + " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?"
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Log.d("Se escribió en: ", "File written to $file")
    }


}
