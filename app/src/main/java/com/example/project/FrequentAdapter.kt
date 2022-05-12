package com.example.project

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.project.databinding.FrequentTransfersItemBinding
import com.example.project.fragments.TransfersFragment
import com.google.firebase.database.*

class FrequentAdapter(
    private val listener: ListenerFrequent,
    private val frequentList: MutableList<FrequentTransfer>,
    private val context: Context,
    private val database: DatabaseReference,
    private val fragment: TransfersFragment
) : RecyclerView.Adapter<FrequentAdapter.FrequentHolder>() {
    class FrequentHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = FrequentTransfersItemBinding.bind(item)

        @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
        fun bind(
            button: FrequentTransfer,
            listener: ListenerFrequent,
            mCtx: Context,
            db: DatabaseReference,
            fragment: TransfersFragment
        ) = with(binding) {
            imgFrequent.setImageResource(button.imageId)
            txtFrequent.text = button.title
            imgSettings.setOnClickListener {
                val popup = PopupMenu(mCtx, binding.imgSettings)
                popup.inflate(R.menu.options_menu_frequent)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.options_rename -> {
                            val bundle = bundleOf("title" to button.title)
                            fragment.findNavController().navigate(R.id.action_transfersFragment_to_renameFragment, bundle)
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
        holder.bind(frequentList[position], listener, context, database, fragment)
    }

    override fun getItemCount(): Int {
        return frequentList.size
    }

    interface ListenerFrequent {
        fun onClick(button: FrequentTransfer)
    }
}