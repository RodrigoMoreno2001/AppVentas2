package com.example.appventas2

data class producto (
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val catalogo: String,
    var enWishlist: Boolean = false,
    var enCarrito: Boolean = false
)