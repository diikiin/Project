package com.example.project.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.project.DBKeys
import com.example.project.MainActivity
import com.example.project.databinding.FragmentTransferToClientBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TransferToClientFragment : Fragment() {
    private var _binding: FragmentTransferToClientBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferToClientBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        val user = firebaseAuth.currentUser?.email.toString().removeSuffix("@gmail.com")

        database.child(DBKeys.Users.toString()).child(user).get().addOnSuccessListener { it ->
            val textCard = it.child("cardId").value.toString()
            database.child(DBKeys.Cards.toString()).child(textCard).get().addOnSuccessListener {
                binding.txtCardValue.text = it.value.toString()
            }
        }

        val client = binding.phoneNumber.text.toString()
        val amount = binding.amount.text.toString().toInt()

        database.child(DBKeys.Users.toString()).child(client)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        val clientCard = snapshot.child("cardId").value.toString()
                        var clientBalance: Int
                        database.child(DBKeys.Cards.toString()).child(clientCard).get()
                            .addOnSuccessListener {
                                clientBalance = it.value as Int
                            }

                        var myCard: String
                        var myBalance: Int
                        database.child(DBKeys.Users.toString()).child(user).get()
                            .addOnSuccessListener { it ->
                                myCard = it.child("cardId").value.toString()
                                database.child(DBKeys.Cards.toString()).child(myCard).get()
                                    .addOnSuccessListener {
                                        myBalance = it.value as Int
                                    }
                            }

                    } else {
                        val error = "Client with this phone number does not exist"
                        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                }
            })

        return binding.root
    }
}