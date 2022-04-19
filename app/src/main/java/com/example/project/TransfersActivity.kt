//package com.example.project
//
////import com.example.project.ui.main.SectionsPagerAdapter
//import android.content.Intent
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import androidx.appcompat.app.AppCompatActivity
//import com.example.project.databinding.ActivityTransfersBinding
//import com.example.tabactivity.ui.main.SectionsPagerAdapter
//import com.google.android.material.tabs.TabLayoutMediator
//
//class TransfersActivity : AppCompatActivity() {
//
//    private val TAB_TITLES = arrayOf(
//        "My transfers", "History"
//    )
//
//    private lateinit var binding: ActivityTransfersBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityTransfersBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val menu: Menu = binding.bottomNavigationView.menu
//        val menuItem: MenuItem = menu.getItem(1)
//        menuItem.isChecked = true
//
//        binding.bottomNavigationView.setOnItemSelectedListener {  item ->
//            when(item.itemId){
//                R.id.home->{
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.transfers->{
//
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
//
//        val viewPager = binding.viewPager
//        val tabLayout = binding.tabs
//        val adapter = SectionsPagerAdapter(supportFragmentManager, lifecycle)
//        viewPager.adapter = adapter
//
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = TAB_TITLES[position]
//        }.attach()
//
//
//    }
//}