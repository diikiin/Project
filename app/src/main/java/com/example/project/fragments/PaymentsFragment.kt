package com.example.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.*
import com.example.project.databinding.FragmentPaymentsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PaymentsFragment : Fragment(), FavouriteAdapter.ListenerFavourite {
    private var _binding: FragmentPaymentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private val user = DBKeys.user!!

    private var favouriteList: MutableList<FavouritePayment> = mutableListOf()
    private var favouriteAdapter: FavouriteAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS)
        init()
        binding.imgPayment1.setOnClickListener {
            findNavController().navigate(R.id.action_paymentsFragment_to_paymentFragment)
        }
        binding.imgPayment2.setOnClickListener {
            findNavController().navigate(R.id.action_paymentsFragment_to_paymentFragment)
        }
        binding.imgPayment3.setOnClickListener {
            findNavController().navigate(R.id.action_paymentsFragment_to_paymentFragment)
        }

        return binding.root
    }

    private fun init() {
        database.child(user).child("favouritePayment")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        binding.txtFavorites.visibility = View.VISIBLE
                        favouriteList.clear()
                        for (dss in snapshot.children) {
                            favouriteList.add(
                                FavouritePayment(
                                    dss.child("imageId").getValue(Int::class.java)!!,
                                    dss.child("title").value.toString(),
                                    dss.child("name").value.toString()
                                )
                            )
                        }
                        favouriteAdapter = FavouriteAdapter(this@PaymentsFragment, favouriteList, context!!, database)
                        binding.apply {
                            rcView.layoutManager = LinearLayoutManager(activity)
                            rcView.adapter = favouriteAdapter
                            rcView.addItemDecoration(
                                DividerItemDecoration(
                                    rcView.context,
                                    DividerItemDecoration.VERTICAL
                                )
                            )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onClick(button: FavouritePayment) {
        val bundle = bundleOf("name" to button.name)
        findNavController().navigate(
            R.id.action_paymentsFragment_to_paymentToFavouriteFragment,
            bundle
        )
    }
}