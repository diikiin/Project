package com.example.project

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FrequentTransfersItemBinding
import com.google.firebase.database.*

class FrequentAdapter(
    private val listener: ListenerFrequent,
    private val frequentList: MutableList<FrequentTransfer>,
    private val context: Context,
    private val database: DatabaseReference
) : RecyclerView.Adapter<FrequentAdapter.FrequentHolder>() {
    class FrequentHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = FrequentTransfersItemBinding.bind(item)

        @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
        fun bind(
            button: FrequentTransfer,
            listener: ListenerFrequent,
            mCtx: Context,
            db: DatabaseReference
        ) = with(binding) {
            imgFrequent.setImageResource(button.imageId)
            txtFrequent.text = button.title
            binding.imgSettings.setOnClickListener {
                val popup = PopupMenu(mCtx, binding.imgSettings)
                popup.inflate(R.menu.options_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.options_rename -> {
                            Toast.makeText(mCtx, "Rename", Toast.LENGTH_SHORT).show()
                            true
                        }
                        R.id.options_delete -> {
                            val query = db.child(DBKeys.user!!).child("frequentTransfer")
                                .orderByChild("title").equalTo(button.title)
                            query.addChildEventListener(object : ChildEventListener {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onChildAdded(
                                    snapshot: DataSnapshot,
                                    previousChild: String?
                                ) {
                                    snapshot.ref.setValue(null)
                                }

                                override fun onChildChanged(
                                    snapshot: DataSnapshot, previousChildName: String?
                                ) {}
                                override fun onChildRemoved(snapshot: DataSnapshot) {
                                    Toast.makeText(mCtx, "Deleted", Toast.LENGTH_SHORT).show()
                                }
                                override fun onChildMoved(
                                    snapshot: DataSnapshot, previousChildName: String?
                                ) {}
                                override fun onCancelled(error: DatabaseError) {}
                            })
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }

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
        holder.bind(frequentList[position], listener, context, database)
    }

    override fun getItemCount(): Int {
        return frequentList.size
    }

    interface ListenerFrequent {
        fun onClick(button: FrequentTransfer)
    }
}