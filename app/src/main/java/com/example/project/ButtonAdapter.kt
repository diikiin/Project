package com.example.project

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.RcButtonItemBinding
import com.example.project.fragments.TransfersFragment


class ButtonAdapter(
    private val listener: Listener,
    private val buttonList: ArrayList<RecyclerButton>
) : RecyclerView.Adapter<ButtonAdapter.ButtonHolder>() {
    inner class ButtonHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = RcButtonItemBinding.bind(item)

        @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
        fun bind(button: RecyclerButton, listener: Listener) = with(binding) {
            imgCard.setImageResource(button.imageId)
            txtCard.text = button.title
            itemView.setOnClickListener {
                listener.onClick(button)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rc_button_item, parent, false)
        return ButtonHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ButtonHolder, position: Int) {
        holder.bind(buttonList[position], listener)
    }

    override fun getItemCount(): Int {
        return buttonList.size
    }

    interface Listener {
        fun onClick(button: RecyclerButton)
    }
}