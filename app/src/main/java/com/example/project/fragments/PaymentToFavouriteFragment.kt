package com.example.project.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
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
    private var bonus = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentToFavouriteBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS)

        binding.amount.doOnTextChanged { text, _, _, _ ->
            amount = text.toString().toIntOrNull()
        }

        database.child(user).get().addOnSuccessListener {
            balance = it.child("card").child("balance").value.toString().toInt()
            binding.txtCardValue.text = balance.toString()
        }

        binding.txtPayment.text = arguments?.getString("name")

        bonusCheck()

        binding.btnPay.setOnClickListener {
            if (!TextUtils.isEmpty(binding.amount.text)) {
                amount = binding.amount.text.toString().toInt()
                validateData()
            } else {
                binding.amount.error = "Please enter an amount of transfer"
            }
        }
        return binding.root
    }

    private fun validateData() {
        when {
            amount!! == 0 || TextUtils.isEmpty(binding.amount.text) -> {
                binding.amount.error = "Please enter an amount of transfer"
            }
            balance!! + bonus < amount!! -> Toast.makeText(
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
            .setValue(balance!! + bonus - amount!!).addOnSuccessListener {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }
        database.child(user).child("bonus").get().addOnSuccessListener {
            database.child(user).child("bonus").setValue(it.value.toString().toInt() - bonus)
        }

        binding.amount.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.btnPay.onEditorAction(EditorInfo.IME_ACTION_DONE)
        findNavController().navigate(R.id.action_paymentToFavouriteFragment_to_paymentsFragment)
    }

    @SuppressLint("SetTextI18n")
    private fun bonusCheck() {
        binding.switchSpendBonuses.setOnCheckedChangeListener { _, isChecked ->
            if (!TextUtils.isEmpty(binding.amount.text) && binding.amount.text.toString() != "0") {
                if (isChecked) {
                    database.child(user).child("bonus").get().addOnSuccessListener {
                        bonus = it.value.toString().toInt()
                        val amountWithBonus: Int
                        if (amount!! <= bonus) {
                            amountWithBonus = 0
                            bonus = amount!!
                        } else {
                            amountWithBonus = amount?.minus(bonus)!!
                        }
                        binding.txtSpentValue.text = "$amountWithBonus + $bonus"
                        binding.txtSpent.visibility = View.VISIBLE
                        binding.txtSpentValue.visibility = View.VISIBLE
                    }
                        .addOnFailureListener {
                            bonus = 0
                            binding.txtSpentValue.text = "$amount + $bonus"
                            binding.txtSpent.visibility = View.VISIBLE
                            binding.txtSpentValue.visibility = View.VISIBLE
                        }

                } else {
                    binding.txtSpent.visibility = View.GONE
                    binding.txtSpentValue.visibility = View.GONE
                    bonus = 0
                }
            } else {
                binding.amount.error = "Please enter an amount of transfer"
            }
        }
    }
}