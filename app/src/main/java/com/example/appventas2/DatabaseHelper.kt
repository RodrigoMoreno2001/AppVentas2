package com.example.appventas2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.appventas2.DatabaseHelper.Companion.COLUMN_USER_CORREO
import com.example.appventas2.DatabaseHelper.Companion.COLUMN_USER_PASSWORD
import com.example.appventas2.DatabaseHelper.Companion.TABLE_USERS
import android.database.sqlite.SQLiteDatabase as SQLiteDatabase1
import com.example.appventas2.DatabaseHelper.Companion.COLUMN_PROD_WISH as COLUMN_PROD_WISH1

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "appventas2.db"
        private const val DATABASE_VERSION = 5

        // Tablas
        const val TABLE_USERS = "usuarios"
        const val TABLE_PRODUCTOS = "productos"
        const val TABLE_HISTORICO = "historico"



        //Columnas Usuarios
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_CORREO = "correo"
        const val COLUMN_USER_PASSWORD = "password"

        //Columnas Productos
        const val COLUMN_PROD_ID = "id"
        const val COLUMN_PROD_NOMBRE = "nombre"
        const val COLUMN_PROD_PRECIO = "precio"
        const val COLUMN_PROD_CAT = "categoria"
        var COLUMN_PROD_WISH = "enwishlist"
        var COLUMN_PROD_CAR = "encarrito"

        //Columnas Historico
        const val COLUMN_H_ID = "id"
        const val COLUMN_H_IDUSER= "iduser"
        const val COLUMN_H_IDPROD = "idprod"
        const val COLUMN_H_FECHA= "fecha"
    }

    override fun onCreate(db: SQLiteDatabase1) {
        Log.d("Database", "SE HA CREADO AL MENOS")




        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_CORREO TEXT NOT NULL UNIQUE,
                $COLUMN_USER_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createUsersTable)



        val createProductosTable = """
            CREATE TABLE $TABLE_PRODUCTOS (
                $COLUMN_PROD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PROD_NOMBRE TEXT NOT NULL,
                $COLUMN_PROD_PRECIO DOUBLE NOT NULL,
                $COLUMN_PROD_CAT TEXT NOT NULL,
                $COLUMN_PROD_WISH TINYINT(1),
                $COLUMN_PROD_CAR TINYINT(1) 
            )
        """.trimIndent()
        db.execSQL(createProductosTable)


        val createHistoricoTable = """
            CREATE TABLE $TABLE_HISTORICO(
                $COLUMN_H_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_H_IDUSER INTEGER NOT NULL,
                $COLUMN_H_IDPROD INTEGER NOT NULL,
                $COLUMN_H_FECHA STRING NOT NULL,
                FOREIGN KEY ($COLUMN_H_IDUSER) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
                FOREIGN KEY ($COLUMN_H_IDPROD) REFERENCES $TABLE_PRODUCTOS($COLUMN_PROD_ID)
            )
        """.trimIndent()
        db.execSQL(createHistoricoTable)


        insertarProductosEjemplo(db)
        insertarUsuarioAdmin(db)
    }

    override fun onUpgrade(p0: SQLiteDatabase1?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


    fun insertarProductosEjemplo(db: SQLiteDatabase1) {
        val productos = listOf(
            producto(0, "Manzana", 0.50, "Frutas", false, false),
            producto(0, "Leche", 1.20, "Lácteos", false, false),
            producto(0, "Pan", 1.00, "Panadería", false, false),
            producto(0, "Jugo de Naranja", 1.50, "Bebidas", false, false),
            producto(0, "Arroz", 2.00, "Granos", false, false),
            producto(0, "Pollo", 5.00, "Carnes", false, false),
            producto(0, "Tomates", 0.80, "Verduras", false, false),
            producto(0, "Queso", 3.50, "Lácteos", false, false),
            producto(0, "Cereal", 2.50, "Desayuno", false, false)
        )

        productos.forEach { prod ->
            val values = ContentValues().apply {
                put(COLUMN_PROD_NOMBRE, prod.nombre)
                put(COLUMN_PROD_PRECIO, prod.precio)
                put(COLUMN_PROD_CAT, prod.catalogo)
                put(COLUMN_PROD_WISH , 0)
                put(COLUMN_PROD_CAR, 0)

            }

            val result = db.insert(TABLE_PRODUCTOS, null, values)
            if (result == -1L) {
                Log.e("DatabaseHelper", "Error al insertar el producto ${prod.nombre}")
            } else {
                Log.d("DatabaseHelper", "Producto ${prod.nombre} insertado correctamente.")
            }
        }
    }


    fun getUserByEmail(email: String): Cursor {
        val db = readableDatabase
        return db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID, COLUMN_USER_CORREO, COLUMN_USER_PASSWORD),
            "$COLUMN_USER_CORREO = ?",
            arrayOf(email.trim().lowercase()),
            null, null, null
        )
    }

    fun validateUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = """
        SELECT COUNT(*) 
        FROM $TABLE_USERS 
        WHERE $COLUMN_USER_CORREO = ? 
        AND $COLUMN_USER_PASSWORD = ?
    """.trimIndent()

        return db.rawQuery(query, arrayOf(email, password)).use { cursor ->
            cursor.moveToFirst() && cursor.getInt(0) > 0
        }
    }

    fun obtenerProductos(): List<producto> {
        val productos = mutableListOf<producto>()
        val db = readableDatabase

        // Realizamos la consulta para obtener todos los productos
        val query = "SELECT * FROM $TABLE_PRODUCTOS"
        val cursor = db.rawQuery(query, null)

        // Si hay resultados, los procesamos

        if (cursor.moveToFirst()) {
            do {
                // Recuperamos los valores de cada columna
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_PROD_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_PROD_NOMBRE))
                val precio = cursor.getDouble(cursor.getColumnIndex(COLUMN_PROD_PRECIO))
                val categoria = cursor.getString(cursor.getColumnIndex(COLUMN_PROD_CAT))

                // Agregamos el producto a la lista
                productos.add(producto(id, nombre, precio, categoria))
            } while (cursor.moveToNext()) // Continuamos con el siguiente producto
        }


        // Cerramos el cursor para liberar recursos

        Log.d("DatabaseHelper", "admin insertado correctamente.")

        cursor.close()
        return productos
    }

    fun obtenerProductosWishlist(): List<producto> {
        val productos = mutableListOf<producto>()
        val db = readableDatabase

        // Realizamos la consulta para obtener todos los productos
        val query = "SELECT * FROM $TABLE_PRODUCTOS WHERE $COLUMN_PROD_WISH1 = 1"
        val cursor = db.rawQuery(query, null)

        // Si hay resultados, los procesamos

        if (cursor.moveToFirst()) {
            do {
                // Recuperamos los valores de cada columna
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_PROD_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_PROD_NOMBRE))
                val precio = cursor.getDouble(cursor.getColumnIndex(COLUMN_PROD_PRECIO))
                val categoria = cursor.getString(cursor.getColumnIndex(COLUMN_PROD_CAT))

                // Agregamos el producto a la lista
                productos.add(producto(id, nombre, precio, categoria))
            } while (cursor.moveToNext()) // Continuamos con el siguiente producto
        }



        // Cerramos el cursor para liberar recursos

        Log.d("DatabaseHelper", "admin insertado correctamente.")

        cursor.close()
        return productos
    }

    fun obtenerProductosCarrito(): List<producto> {
        val productos = mutableListOf<producto>()
        val db = readableDatabase

        // Realizamos la consulta para obtener todos los productos
        val query = "SELECT * FROM $TABLE_PRODUCTOS WHERE $COLUMN_PROD_CAR = 1"
        val cursor = db.rawQuery(query, null)

        // Si hay resultados, los procesamos

        if (cursor.moveToFirst()) {
            do {
                // Recuperamos los valores de cada columna
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_PROD_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_PROD_NOMBRE))
                val precio = cursor.getDouble(cursor.getColumnIndex(COLUMN_PROD_PRECIO))
                val categoria = cursor.getString(cursor.getColumnIndex(COLUMN_PROD_CAT))

                // Agregamos el producto a la lista
                productos.add(producto(id, nombre, precio, categoria))
            } while (cursor.moveToNext()) // Continuamos con el siguiente producto
        }



        // Cerramos el cursor para liberar recursos

        Log.d("DatabaseHelper", "admin insertado correctamente.")

        cursor.close()
        return productos
    }

    fun actualizarWishlist(idProducto: Int, enWishlist: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROD_WISH1, if (enWishlist) 1 else 0) // 🔥 Usamos la constante correcta
        }
        db.update(TABLE_PRODUCTOS, values, "$COLUMN_PROD_ID = ?", arrayOf(idProducto.toString()))
        db.close()
    }

    fun actualizarCarrito(idProducto: Int, encarrito: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROD_CAR, if (encarrito) 1 else 0) // 🔥 Usamos la constante correcta
        }
        db.update(TABLE_PRODUCTOS, values, "$COLUMN_PROD_ID = ?", arrayOf(idProducto.toString()))
        db.close()
    }



    // Canciones
    fun insertProducto(nombre: String, precio: Double, categoria: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROD_NOMBRE, nombre)
            put(COLUMN_PROD_PRECIO, precio)
            put(COLUMN_PROD_CAT, categoria)
        }
        return db.insert(TABLE_PRODUCTOS, null, values)
    }



}

    private fun insertarUsuarioAdmin(db: SQLiteDatabase1) {
        val values = ContentValues().apply {
            put(COLUMN_USER_CORREO, "admin")
            put(COLUMN_USER_PASSWORD, "admin")
        }
        db.insert(TABLE_USERS, null, values)
        Log.d("DatabaseHelper", "admin insertado correctamente.")

    }

