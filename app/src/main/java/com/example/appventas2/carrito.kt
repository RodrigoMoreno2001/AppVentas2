package com.example.appventas2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventas2.R.id.maincar


class carrito : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_carrito)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(maincar)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonvolver = findViewById<Button>(R.id.volvercar)

        botonvolver.setOnClickListener {
            val intent = Intent(this@carrito, catalogo::class.java)
            startActivity(intent)
            finish()
        }

        /* !!!!!!!!!!!!!!!!! ACTIVITY O FRAGMENT?? !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        val botonhistorico = findViewById<Button>(R.id.historico)

        botonvolver.setOnClickListener {
            val intent = Intent(this@carrito, catalogo::class.java)
            startActivity(intent)
            finish()
        }*/


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbhelper = DatabaseHelper(this)
        val listaProductos = dbhelper.obtenerProductosCarrito()


        recyclerView.adapter = productoadapter(
            listaProductos,
            context = this,  // 🔥 Pasamos el contexto aquí
            onAddToCart = { producto ->
                producto.enCarrito = false
                dbhelper.actualizarCarrito(producto.id, producto.enCarrito)
                Toast.makeText(this, "${producto.nombre} se ha quitado del carrito.", Toast.LENGTH_SHORT).show()
            },
            onWishlistChanged = { producto, isChecked ->
                dbhelper.actualizarWishlist(producto.id, isChecked)
                producto.enWishlist = isChecked
            },
            isWishlistActivity = false,
            onDelete = {

            }
        )



    }
}