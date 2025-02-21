package com.example.appventas2

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater


class productoadapter(
    private val productos: List<producto>,
    private val context: Context,  // 🔥 Guardamos el contexto
    private val onAddToCart: (producto) -> Unit,
    private val onWishlistChanged: (producto, Boolean) -> Unit,
    private val isWishlistActivity: Boolean,
    private val onDelete: (producto) -> Unit
) : RecyclerView.Adapter<productoadapter.ProductoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }


    inner class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProducto: ImageView = view.findViewById(R.id.ivProducto)
        val idProducto : TextView = view.findViewById(R.id.id)
        val tvNombre: TextView = view.findViewById(R.id.tvNombreProducto)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoriaProducto)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecioProducto)
        val checkBoxWishlist: CheckBox = view.findViewById(R.id.checkBox)
        val btnAddToCart: Button = view.findViewById(R.id.button)
        val borrar : Button = view.findViewById(R.id.borraradmin)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.idProducto.text =producto.id.toString()
        holder.tvNombre.text = producto.nombre
        holder.tvCategoria.text = producto.catalogo
        holder.tvPrecio.text = "${producto.precio}€"

        // 🚨 Desactivar el listener para evitar llamadas involuntarias
        holder.checkBoxWishlist.setOnCheckedChangeListener(null)

        // 📌 Si estamos en WishlistActivity, se marca el checkbox por defecto
        holder.checkBoxWishlist.isChecked = isWishlistActivity || producto.enWishlist

        // ✅ Reactivar el listener después de actualizar la UI
        holder.checkBoxWishlist.setOnCheckedChangeListener { _, isChecked ->
            onWishlistChanged(producto, isChecked)
        }

        // Botón de añadir al carrito
        holder.btnAddToCart.setOnClickListener {
            onAddToCart(producto)
        }
    }

     fun onBindViewHolderAdmin(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        holder.idProducto.text = producto.id.toString()
        holder.tvNombre.text = producto.nombre
        holder.tvCategoria.text = producto.catalogo
        holder.tvPrecio.text = "${producto.precio}€"


    }

    override fun getItemCount(): Int {
        return productos.size
    }
}
