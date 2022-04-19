package com.example.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.example.project.Card
import com.example.project.Client
import com.example.project.R
import com.example.project.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val USER_KEY = "Users"
    private val CARD_KEY = "Cards"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = Firebase.database.reference
        actions()
        firebaseAuth = FirebaseAuth.getInstance()
        database.child(USER_KEY).child(firebaseAuth.currentUser?.uid.toString()).get().addOnSuccessListener {
            if (it.child("bonus").exists()) {
                binding.txtBonusValue.text = it.child("bonus").value.toString()
            } else
                binding.txtBonusValue.text = "0"
        }


        binding.addToDB.setOnClickListener {
//            val user = Firebase.auth.currentUser?.email.toString().removeSuffix("@gmail.com")
//            val client = Client("Dauren", "Kabyl", 20)
//            client.bonus = 100
//            database.child(USER_KEY).child(user).setValue(client).addOnSuccessListener {
//                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
//            }.addOnFailureListener {
//                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
//            }

            database.child(CARD_KEY).child("1234 5687 1111 2222").setValue(100000).addOnSuccessListener {
                Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }


    private fun actions() = with(binding) {
        imgQR.setOnClickListener {
            NavHostFragment.findNavController(this@HomeFragment)
                .navigate(R.id.action_homeFragment_to_QRFragment)
        }
        imgPayments.setOnClickListener {
//            Navigation.findNavController(this.root).navigate(R.id.action_homeFragment_to_paymentsFragment)
            NavHostFragment.findNavController(this@HomeFragment)
                .navigate(R.id.action_homeFragment_to_paymentsFragment)

        }

    }

}