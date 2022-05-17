package com.example.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.project.DBKeys
import com.example.project.R
import com.example.project.databinding.FragmentRenameBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RenameFragment : Fragment() {
    private lateinit var binding: FragmentRenameBinding

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRenameBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS).child(DBKeys.user!!)

        val oldTitle = arguments?.getString("title")

        binding.btnNewTitle.setOnClickListener {
            val newTitle = binding.txtNewTitle.text.toString()
            val query = database.child("frequentTransfer").orderByChild("title").equalTo(oldTitle)

            query.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(
                    snapshot: DataSnapshot,
                    previousChild: String?
                ) {
                    snapshot.ref.child("title").setValue(newTitle)
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot, previousChildName: String?
                ) {
                    Toast.makeText(activity, "Renamed", Toast.LENGTH_SHORT).show()
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(
                    snapshot: DataSnapshot, previousChildName: String?
                ) {}
                override fun onCancelled(error: DatabaseError) {}
            })

            binding.txtRenameLabel.onEditorAction(EditorInfo.IME_ACTION_DONE)
            binding.btnNewTitle.onEditorAction(EditorInfo.IME_ACTION_DONE)
            findNavController().navigate(R.id.action_renameFragment_to_transfersFragment)
        }
        return binding.root
    }
}