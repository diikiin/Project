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
        return binding.root
    }

    private fun actions() = with(binding) {
        imgQR.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_QRFragment)
        }
    }
}