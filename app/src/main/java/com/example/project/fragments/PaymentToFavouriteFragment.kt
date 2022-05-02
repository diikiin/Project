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
import com.example.project.R
import com.example.project.databinding.FragmentPaymentToFavouriteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PaymentToFavouriteFragment : Fragment() {
    private var _binding: FragmentPaymentToFavouriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private val user = DBKeys.user!!
    private var balance: Int? = null
    private var amount: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentToFavouriteBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS)

        database.child(user).get().addOnSuccessListener {
            balance = it.child("card").child("balance").value.toString().toInt()
            binding.txtCardValue.text = balance.toString()
        }

        binding.txtPayment.text = arguments?.getString("name")

        binding.btnPay.setOnClickListener {
            amount = binding.amount.text.toString().toInt()
            validateData()
        }
        return binding.root
    }

    private fun validateData() {
        when {
            amount!! == 0 || TextUtils.isEmpty(amount.toString()) -> {
                binding.amount.error = "Please enter an amount of transfer"
            }
            balance!! <= amount!! -> Toast.makeText(
                activity,
                "You do not have so much money",
                Toast.LENGTH_SHORT
            )
                .show()
            else -> {
                setBalance()
            }
        }
    }

    private fun setBalance() {
        database.child(user).child("card").child("balance")
            .setValue(balance!! - amount!!).addOnSuccessListener {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }

        binding.amount.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.btnPay.onEditorAction(EditorInfo.IME_ACTION_DONE)
        findNavController().navigate(R.id.action_paymentToFavouriteFragment_to_paymentsFragment)
    }
}