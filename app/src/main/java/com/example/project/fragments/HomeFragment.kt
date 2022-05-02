package com.example.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.project.*
import com.example.project.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS)
        actions()
        val user = DBKeys.user
        database.child(user!!).get().addOnSuccessListener { it ->
            binding.txtBonusValue.text = it.child("bonus").value.toString()
            val textCard = it.child("card").child("cardNumber").value.toString()
            binding.txtCardCode.text = textCard.replaceRange(0, 14, "*")
            binding.txtCardValue.text = it.child("card").child("balance").value.toString()
        }

        binding.addToDB.setOnClickListener {
            database.child("+77025559900").setValue(
                Client(
                    "Daniyar",
                    "Nurzhanov",
                    45,
                    Card("1234 5687 9999 0000", 100000),
                    bonus = 100,
                    password = "admin123"
                )
            ).addOnSuccessListener {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }
            database.child("+7777").setValue(
                Client(
                    "Dauren",
                    "Kabyl",
                    20,
                    Card("1234 5687 1111 2222", 100000),
                    bonus = 100,
                    frequentTransfer = ArrayList(listOf(FrequentTransfer(R.drawable.ic_mastercard, "Wife", "+77025557788"))),
                    favouritePayment = ArrayList(listOf(FavouritePayment(R.drawable.ic_payment, "Public utilities"))),
                    password = "admin123"
                )
            ).addOnSuccessListener {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }
            database.child("+77025557788").setValue(
                Client(
                    "Madina",
                    "Alieva",
                    21,
                    Card("1234 5687 7777 8888", 100000)
                )
            ).addOnSuccessListener {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }
            database.child("+77025555566").setValue(
                Client(
                    "Kabdol",
                    "Beiymbet",
                    35,
                    Card("1234 5687 5555 6666", 100000),
                    bonus = 100
                )
            ).addOnSuccessListener {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }
            database.child("+77025553344").setValue(
                Client(
                    "Aliya",
                    "Zaparova",
                    30,
                    Card("1234 5687 3333 4444", 100000)
                )
            ).addOnSuccessListener {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun actions() = with(binding) {
        imgQR.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_QRFragment)
        }
    }
}