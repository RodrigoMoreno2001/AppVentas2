package com.example.appventas2

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class iniciarsesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciarsesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val botonVolver = findViewById<Button>(R.id.iniciarvolver)
        val botonIniciar = findViewById<Button>(R.id.iniciarsiguiente)

        botonVolver.setOnClickListener {
            finish()
        }

        botonIniciar.setOnClickListener {
            val emailField = findViewById<EditText>(R.id.editTextTextEmailAddress2)
            val passwordField = findViewById<EditText>(R.id.editTextTextEmailAddress3)

            val email = emailField.text.toString().trim().lowercase()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val dbHelper = DatabaseHelper(this)
                val isValidUser = dbHelper.validateUser(email, password)

                if (isValidUser) {

                    if(email.equals("admin") && password.equals("admin")){
                        Toast.makeText(this, "Entrando en panel de admin", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@iniciarsesion, adminpanel::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        dbHelper.getUserByEmail(email).use { cursor ->
                            if (cursor.moveToFirst()) {
                                val userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID))

                                // Guardar userId en SharedPreferences
                                val prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                prefs.edit().putInt("userId", userId).apply()

                                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@iniciarsesion, catalogo::class.java)
                                startActivity(intent)
                                finish() // Cierra la actividad de login
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
