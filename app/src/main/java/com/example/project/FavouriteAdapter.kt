package com.example.project

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FavouritePaymentItemBinding

class FavouriteAdapter(
    private val listener: ListenerFavourite,
    private val favouriteList: MutableList<FavouritePayment>
):RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder>() {
    inner class FavouriteHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = FavouritePaymentItemBinding.bind(item)

        @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
        fun bind(button: FavouritePayment, listener: ListenerFavourite) = with(binding) {
            imgFavourite.setImageResource(button.imageId)
            txtFavourite.text = button.title
            itemView.setOnClickListener {
                listener.onClick(button)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.favourite_payment_item, parent, false)
        return FavouriteHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteHolder, position: Int) {
        holder.bind(favouriteList[position], listener)
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }

    interface ListenerFavourite {
        fun onClick(button: FavouritePayment)
    }
}