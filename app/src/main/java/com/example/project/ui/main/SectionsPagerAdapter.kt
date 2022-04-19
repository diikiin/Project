//package com.example.tabactivity.ui.main
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.lifecycle.Lifecycle
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.example.project.fragments.TransfersFragment
//import com.example.project.fragments.TransfersHistoryFragment
//
//private const val NUM_TABS = 2
//
//class SectionsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
//    FragmentStateAdapter(fm, lifecycle) {
//    override fun getItemCount(): Int = NUM_TABS
//
//    override fun createFragment(position: Int): Fragment {
//        return if (position == 0){
//            TransfersFragment()
//        }else{
//            TransfersHistoryFragment()
//        }
//    }
//}