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
import com.example.appventas2.R.id.mainadmin

class adminpanel : AppCompatActivity()  {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_adminpanel)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(mainadmin)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val dbh = DatabaseHelper(this)
        val listaProductos = dbh.obtenerProductos()

        val botonquitar = findViewById<Button>(R.id.borraradmin)


        val botonanyadir = findViewById<Button>(R.id.anyadiradmin)


        botonanyadir.setOnClickListener{
            val nombreprod = findViewById<EditText>(R.id.nombreInput)
            val aux = findViewById<EditText>(R.id.precioProducto)
            val precio: Double? = aux.text.toString().toDoubleOrNull()
            val catprod = findViewById<EditText>(R.id.categoriaInput)
            if ((nombreprod == null)|| (precio == null) || (catprod == null)){
                Toast.makeText(this, "Complete todos los campos para añadir", Toast.LENGTH_SHORT).show()
            }else{
                val dbh = DatabaseHelper(this)
                dbh.insertProducto(nombreprod.toString(), precio, catprod.toString())
                Toast.makeText(this, "Producto añadido", Toast.LENGTH_SHORT).show()
                recreate()
            }
        }



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProductosadmin)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = productoadapter(
            listaProductos,
            context = this,
            onAddToCart = { producto ->
                    //
            },
            onWishlistChanged = { producto, isChecked ->

            },
            isWishlistActivity = false,
            onDelete = {

            }
        )

    }


}