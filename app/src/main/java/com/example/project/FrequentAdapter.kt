package com.example.project

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FrequentTransfersItemBinding

class FrequentAdapter(
    private val listener: ListenerFrequent,
    private val frequentList: ArrayList<FrequentTransfer>
) : RecyclerView.Adapter<FrequentAdapter.FrequentHolder>() {
    inner class FrequentHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = FrequentTransfersItemBinding.bind(item)

        @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
        fun bind(button: FrequentTransfer, listener: ListenerFrequent) = with(binding) {
            imgFrequent.setImageResource(button.imageId)
            txtFrequent.text = button.title
            itemView.setOnClickListener {
                listener.onClick(button)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrequentHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.frequent_transfers_item, parent, false)
        return FrequentHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FrequentHolder, position: Int) {
        holder.bind(frequentList[position], listener)
    }

    override fun getItemCount(): Int {
        return frequentList.size
    }

    interface ListenerFrequent {
        fun onClick(button: FrequentTransfer)
    }
}