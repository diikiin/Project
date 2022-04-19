package com.example.project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.project.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        val navHostFragment = binding.fragmentContainerView.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController
        
        binding.bottomNavigationView.setupWithNavController(navController)

//        val navHostFragment = binding.fragmentContainerView.getFragment<NavHostFragment>()
//        val navController = navHostFragment.navController
//
//        binding.bottomNavigationView.setupWithNavController(navController)
//        val menu: Menu = binding.bottomNavigationView.menu
//        val menuItem: MenuItem = menu.getItem(0)
//        menuItem.isChecked = true
//
//        binding.bottomNavigationView.setOnItemSelectedListener {  item ->
//            when(item.itemId){
//                R.id.home->{
//
//                }
//                R.id.transfers->{
//                    val intent = Intent(this, TransfersActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.payments-> {
//
//                }
//                R.id.menu->{
//
//                }
//            }
//            return@setOnItemSelectedListener false
//        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.signOut()
    }
}