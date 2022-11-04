package com.example.shareapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shareapp.R
import com.example.shareapp.ResponseModels.Product
import kotlinx.android.synthetic.main.activity_details.view.*
import kotlinx.android.synthetic.main.one_product_list_row.view.*
import kotlinx.android.synthetic.main.one_product_list_row.view.distance_details
import kotlinx.android.synthetic.main.one_product_list_row.view.sender_name_details
import kotlinx.android.synthetic.main.one_product_list_row.view.subject_details

class ProductAdapter(
    private val context: Context,
    private val products: List<Product>,
    private val listener: OneItemClickListener
    ) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_product_list_row, parent, false)
        return ViewHolder(view)
    }

    // one_list_row csatolom az ertekeket
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.donator.text = products[position].getDonator()
        holder.product.text = products[position].getProduct()
        holder.distance.text = products[position].getDistance().toString()
        holder.expiration.text = products[position].getExpiration()

        // image load
        Glide.with(context)
            .load(products[position].getImage()?.toUri())
            .centerCrop()
            .error(R.drawable.no_image)
            .into(holder.image);
    }

    override fun getItemCount() = products.size

    // inner class hogy elerjem a listenert
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        val donator: TextView = itemView.sender_name_details
        val product: TextView = itemView.subject_details
        val distance: TextView = itemView.distance_details
        val expiration: TextView = itemView.exp_details
        val image: ImageView = itemView.product_image
    }
    interface OneItemClickListener{
        fun onItemClick(position: Int)
    }
}
