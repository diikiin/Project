package com.example.project

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var email = ""
    private var password = ""
    private var password2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        binding.btnRegister.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        email = binding.phoneNumber.text.toString().trim() + "@gmail.com"
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
                firebaseRegister()
            }
        }
    }

    private fun firebaseRegister() {
        val text = email.removeSuffix("@gmail.com")
        database.child(DBKeys.Users.toString()).child(text)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
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