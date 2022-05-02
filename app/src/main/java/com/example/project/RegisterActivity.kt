package com.example.project

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var database: DatabaseReference

    private var phone = ""
    private var password = ""
    private var password2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference(DBKeys.USERS)

        binding.btnRegister.setOnClickListener {
            validateData()
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateData() {
        phone = binding.phoneNumber.text.toString().trim()
        password = binding.password.text.toString().trim()
        password2 = binding.password2.text.toString().trim()

        when {
            TextUtils.isEmpty(phone) -> {
                binding.phoneNumber.error = "Please enter a phone number"
            }
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
                firebaseRegister()
            }
        }
    }

    private fun firebaseRegister() {
        database.child(phone)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        if (snapshot.child("password").value == null) {
                            database.child(phone).child("password").setValue(password)
                            DBKeys.user = phone
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            finish()
                        } else {
                            val error = "Account with this number already created"
                            Toast.makeText(this@RegisterActivity, error, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val error = "Client with this phone number does not exist"
                        Toast.makeText(this@RegisterActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RegisterActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}