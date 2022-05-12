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
import com.example.project.databinding.FavouritePaymentItemBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class FavouriteAdapter(
    private val listener: ListenerFavourite,
    private val favouriteList: MutableList<FavouritePayment>,
    private val context: Context,
    private val database: DatabaseReference
):RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder>() {
    inner class FavouriteHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = FavouritePaymentItemBinding.bind(item)

        @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
        fun bind(button: FavouritePayment,
                 listener: ListenerFavourite,
                 mCtx: Context,
                 db: DatabaseReference
        ) = with(binding) {
            imgFavourite.setImageResource(button.imageId)
            txtFavourite.text = button.title
            imgSettings.setOnClickListener {
                val popup = PopupMenu(mCtx, binding.imgSettings)
                popup.inflate(R.menu.options_menu_favourite)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.options_delete -> {
                            val query = db.child(DBKeys.user!!).child("favouritePayment")
                                .orderByChild("title").equalTo(button.title)
                            query.addChildEventListener(object : ChildEventListener {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteAdapter.FavouriteHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.favourite_payment_item, parent, false)
        return FavouriteHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteAdapter.FavouriteHolder, position: Int) {
        holder.bind(favouriteList[position], listener, context, database)
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }

    interface ListenerFavourite {
        fun onClick(button: FavouritePayment)
    }
}