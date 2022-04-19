package com.example.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.ButtonAdapter
import com.example.project.R
import com.example.project.RecyclerButton
import com.example.project.databinding.FragmentTransfersBinding


class TransfersFragment : Fragment(), ButtonAdapter.Listener {
    private var _binding: FragmentTransfersBinding?=null
    private val binding get() = _binding!!

    private val buttonList = ArrayList(
        listOf(
            RecyclerButton(R.drawable.ic_transfer, "Between my accounts"),
            RecyclerButton(R.drawable.ic_user, "To Dikin Client"),
            RecyclerButton(R.drawable.ic_credit_card, "To another bank card")
        )
    )

    private val adapter = ButtonAdapter(this, buttonList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransfersBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
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

    override fun onClick(button: RecyclerButton) {
        when (button.title) {
            "Between my accounts" -> Toast.makeText(activity, "Phone number", Toast.LENGTH_SHORT).show()
            "To Dikin Client" -> Toast.makeText(activity, "Phone number", Toast.LENGTH_SHORT).show()
            "To another bank card" -> Toast.makeText(activity, "Change password", Toast.LENGTH_SHORT)
                .show()
        }
    }
}