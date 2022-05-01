package com.example.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.project.DBKeys
import com.example.project.R
import com.example.project.databinding.FragmentEnterPasswordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EnterPasswordFragment : Fragment() {
    private var _binding: FragmentEnterPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS)
        binding.btnPassword.setOnClickListener {
            val password = binding.txtPassword.text.toString()
            database.child(DBKeys.user!!).get().addOnSuccessListener {
                if (it.child("password").value.toString() == password) {
                    binding.btnPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
                    findNavController().navigate(R.id.action_enterPasswordFragment_to_changePasswordFragment)
                }
                else{
                    Toast.makeText(activity, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }
}