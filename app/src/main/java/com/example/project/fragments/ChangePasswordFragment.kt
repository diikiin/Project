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
import com.example.project.databinding.FragmentChangePasswordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    private var password = ""
    private var password2 = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS)
        binding.btnChangePassword.setOnClickListener {
            checkPassword()
        }
        return binding.root
    }

    private fun checkPassword(){
        password = binding.password.text.toString().trim()
        password2 = binding.password2.text.toString().trim()
        when {
            TextUtils.isEmpty(password) -> {
                binding.password.error = "Please enter a password"
            }
            password.length < 6 -> {
                binding.password.error = "Password must at least 6 characters long"
            }
            TextUtils.isEmpty(password2) -> {
                binding.password.error = "Please repeat a password"
            }
            password != password2 -> {
                binding.password2.error = "Passwords not equal"
            }
            else -> {
                confirmNewPassword()
            }
        }
    }

    private fun confirmNewPassword(){
        database.child(DBKeys.user!!).child("password").setValue(password)
        binding.btnChangePassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.password.onEditorAction(EditorInfo.IME_ACTION_DONE)
        binding.password2.onEditorAction(EditorInfo.IME_ACTION_DONE)
        Toast.makeText(activity, "Password changed", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_changePasswordFragment_to_menuFragment)
    }
}