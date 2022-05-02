package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.project.databinding.ActivityLoginBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var database: DatabaseReference
    private var phone = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference(DBKeys.USERS)
        checkUser()

        binding.btnLogin.setOnClickListener {
            validateData()
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun validateData() {
        phone = binding.phoneNumber.text.toString().trim()
        password = binding.password.text.toString().trim()

        when {
            TextUtils.isEmpty(phone) -> {
                binding.phoneNumber.error = "Please enter a phone number"
            }
            TextUtils.isEmpty(password) -> {
                binding.password.error = "Please enter a password"
            }
            else -> {
                firebaseLogin()
            }
        }
    }

    private fun firebaseLogin() {
        database.child(phone).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    if (snapshot.child("password").value.toString() == password) {
                        DBKeys.user = phone
                        startMainActivity()
                    } else {
                        val error = "Incorrect password"
                        Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val error = "Client with this phone number does not exist"
                    Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkUser() {
        val user = DBKeys.user
        if (user != null) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}