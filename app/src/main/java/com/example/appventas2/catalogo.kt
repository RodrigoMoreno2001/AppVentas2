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
import com.example.appventas2.R.id.mainCAT

class catalogo : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(mainCAT)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val botonwishlist = findViewById<Button>(R.id.wishlistcat)

        botonwishlist.setOnClickListener {
            val intent = Intent(this@catalogo, wishlist::class.java)
            startActivity(intent)
            finish()
        }

        val botoncarro = findViewById<Button>(R.id.carritocat)

        botoncarro.setOnClickListener {
            val intent = Intent(this@catalogo, carrito::class.java)
            startActivity(intent)
            finish()
        }

        val dbHelper = DatabaseHelper(this)
        val listaProductos = dbHelper.obtenerProductos()



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = productoadapter(
            listaProductos,
            context = this,  // 🔥 Pasamos el contexto aquí
            onAddToCart = { producto ->
                producto.enCarrito = true
                dbHelper.actualizarCarrito(producto.id, producto.enCarrito)
                Toast.makeText(this, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
            },
            onWishlistChanged = { producto, isChecked ->
                dbHelper.actualizarWishlist(producto.id, isChecked)
            },
            isWishlistActivity = false,
            onDelete = {

            }
        )





    }

}




