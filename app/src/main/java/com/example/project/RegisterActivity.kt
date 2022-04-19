package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.project.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""
    private var password2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            validateData()
        }
    }

    private fun validateData(){
        email = binding.phoneNumber.text.toString().trim() + "@gmail.com"
        password = binding.password.text.toString().trim()
        password2 = binding.password2.text.toString().trim()

//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            binding.phoneNumber.error = "Invalid phone number"
//        }
        when {
            TextUtils.isEmpty(password) -> {
                binding.password.error = "Please enter a password"
            }
            password.length<6 -> {
                binding.password.error = "Password must at least 6 characters long"
            }
            TextUtils.isEmpty(password2) -> {
                binding.password.error = "Please repeat a password"
            }
            password != password2 -> {
                binding.password2.error = "Passwords not equal"
            }
            else -> {
                firebaseRegister()
            }
        }
    }

    private fun firebaseRegister(){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}