package com.example.appventas2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appventas2.R.id.mainwish

class wishlist : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_wishlist)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(mainwish)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val botonvolver = findViewById<Button>(R.id.volverwish)

        botonvolver.setOnClickListener {
            val intent = Intent(this@wishlist, catalogo::class.java)
            startActivity(intent)
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbhelper = DatabaseHelper(this)
        val listaProductos = dbhelper.obtenerProductosWishlist()


        recyclerView.adapter = productoadapter(
            listaProductos,
            context = this,  // 🔥 Pasamos el contexto aquí
            onAddToCart = { producto ->
                Toast.makeText(this, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
                producto.enCarrito = true
            },
            onWishlistChanged = { producto, isChecked ->
                dbhelper.actualizarWishlist(producto.id, isChecked)
                producto.enWishlist = isChecked
            },
            isWishlistActivity = true,
            onDelete = {

            }
        )



    }
}