package com.example.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.*
import com.example.project.databinding.FragmentTransfersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class TransfersFragment : Fragment(), ButtonAdapter.Listener, FrequentAdapter.ListenerFrequent {
    private var _binding: FragmentTransfersBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var user: String

    private val buttonList = ArrayList(
        listOf(
            RecyclerButton(R.drawable.ic_transfer, "Between my accounts"),
            RecyclerButton(R.drawable.ic_user, "To Dikin Client"),
            RecyclerButton(R.drawable.ic_credit_card, "To another bank card")
        )
    )
    private val adapter = ButtonAdapter(this, buttonList)

    private var frequentList: MutableList<FrequentTransfer> = arrayListOf()
    private var frequentAdapter: FrequentAdapter? = null
    private var stringList: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransfersBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        database = Firebase.database.getReference(DBKeys.USERS)
        user = firebaseAuth.currentUser?.email.toString().removeSuffix("@gmail.com")

        init1()
        init2()
        return binding.root
    }

    private fun init1() {
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(activity)
            rcView.adapter = adapter
            rcView.addItemDecoration(
                DividerItemDecoration(
                    rcView.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun init2() {
        database.child(user).child("frequentTransfer")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        binding.txtFrequent.visibility = View.VISIBLE
                        snapshot.children.forEach {
                            it.getValue(FrequentTransfer::class.java)
                                ?.let { it1 -> frequentList.add(it1) }
                        }
//                        for (dss in snapshot.children) {
//                            frequentList.add(
//                                FrequentTransfer(
//                                    dss.child("imageId").getValue(Int::class.java)!!,
//                                    dss.child("title").toString(),
//                                    dss.child("phoneNumber").toString()
//                                )
//                            )
//                            stringList?.add(dss.child("title").toString())
//                        }
                        Toast.makeText(activity, frequentList.toString(), Toast.LENGTH_SHORT).show()
//                        frequentAdapter = FrequentAdapter(this@TransfersFragment, frequentList!!)
//                        binding.apply {
//                            rcView2.layoutManager = LinearLayoutManager(activity)
//                            rcView2.adapter = frequentAdapter
//                            rcView2.addItemDecoration(
//                                DividerItemDecoration(
//                                    rcView2.context,
//                                    DividerItemDecoration.VERTICAL
//                                )
//                            )
//                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onClick(button: RecyclerButton) {
        when (button.title) {
            "Between my accounts" -> Toast.makeText(
                activity,
                "Between my accounts",
                Toast.LENGTH_SHORT
            ).show()

            "To Dikin Client" -> NavHostFragment.findNavController(this)
                .navigate(R.id.action_transfersFragment_to_transferToClientFragment)

            "To another bank card" -> Toast.makeText(
                activity,
                "To another bank card",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClick(button: FrequentTransfer) {
        TODO("Not yet implemented")
    }
}