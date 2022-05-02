package com.example.project.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.project.DBKeys
import com.example.project.FrequentTransfer
import com.example.project.R
import com.example.project.databinding.FragmentTransferToClientBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TransferToClientFragment : Fragment() {
    private var _binding: FragmentTransferToClientBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    private lateinit var user: String
    private var balance: Int? = null
    private var amount: Int? = null
    private lateinit var client: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferToClientBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS)
        user = DBKeys.user!!

        database.child(user).get().addOnSuccessListener {
            balance = it.child("card").child("balance").value.toString().toInt()
            binding.txtCardValue.text = balance.toString()
        }

        binding.btnTransfer.setOnClickListener {
            amount = binding.amount.text.toString().toInt()
            client = binding.phoneNumber.text.toString()

            validateData()
        }
        return binding.root
    }

    private fun validateData() {
        when {
            TextUtils.isEmpty(client) -> {
                binding.phoneNumber.error = "Please enter a phone number"
            }
            amount!! == 0 || TextUtils.isEmpty(amount.toString()) -> {
                binding.amount.error = "Please enter an amount of transfer"
            }
            balance!! <= amount!! -> Toast.makeText(activity, "You do not have so much money", Toast.LENGTH_SHORT)
                .show()
            else -> {
                setBalance()
            }
        }
    }

    private fun setBalance() {
        database.child(client)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        database.child(user).child("card").child("balance")
                            .setValue(balance!! - amount!!).addOnSuccessListener {
                                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT)
                                    .show()
                            }.addOnFailureListener {
                                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        database.child(user).get().addOnSuccessListener {
                            balance = it.child("card").child("balance").value.toString().toInt()
                            binding.txtCardValue.text = balance.toString()
                        }

                        binding.btnTransfer.onEditorAction(EditorInfo.IME_ACTION_DONE)
                        switchCheck()

                        findNavController().navigate(R.id.action_transferToClientFragment_to_transfersFragment)
                    } else {
                        val error = "Client with this phone number does not exist"
                        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun switchCheck(){
        if (binding.switchFrequent.isChecked){
            var name:String = client
            database.child(client).get().addOnSuccessListener {
                name = it.child("firstName").value.toString() + " " + it.child("lastName").value.toString()
            }
            val frequent = FrequentTransfer(R.drawable.ic_mastercard, name, name, client)
            database.child(user).child("frequentTransfer").push().setValue(frequent)
        }
    }
}