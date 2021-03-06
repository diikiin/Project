package com.example.project.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.*
import com.example.project.databinding.FragmentMenuBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MenuFragment : Fragment(), ButtonAdapter.Listener {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val buttonList = ArrayList(
        listOf(
            RecyclerButton(R.drawable.ic_notification, "Notifications"),
            RecyclerButton(R.drawable.ic_lock, "Change password"),
            RecyclerButton(R.drawable.ic_exit, "Sign out")
        )
    )
    private val adapter = ButtonAdapter(this, buttonList)

    private val CHANNEL_ID = "channel_id_01"
    private val notificationId = 101

    private lateinit var database: DatabaseReference

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        database = Firebase.database.getReference(DBKeys.USERS)
        database.child(DBKeys.user!!).get().addOnSuccessListener {
            binding.txtName.text = it.child("firstName").value.toString() + ' ' +
                    it.child("lastName").value.toString()
        }
        binding.txtPhoneNumber.text = DBKeys.user
        init()
        createNotificationChannel()
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
            "Notifications" -> notificationDialog()
            "Change password" -> findNavController().navigate(R.id.action_menuFragment_to_enterPasswordFragment)
            "Sign out" -> signOutDialog()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val builder = this.context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Notifications enabled")
                .setContentText("You enabled notifications")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        with(this.context?.let { NotificationManagerCompat.from(it) }) {
            builder?.let { this?.notify(notificationId, it.build()) }
        }
    }

    private fun checkUser() {
        if (DBKeys.user == null) {
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun signOut() {
        DBKeys.user = null
        checkUser()
    }

    @SuppressLint("SetTextI18n")
    private fun notificationDialog() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.activity_custom_dialog)
        val dialogText = dialog.window!!.findViewById<TextView>(R.id.textViewDialog)
        dialogText.text = "Do you want to enable notifications?"
        val btnYes = dialog.window!!.findViewById<Button>(R.id.buttonYes)
        val btnNo = dialog.window!!.findViewById<Button>(R.id.buttonNo)
        btnYes.setOnClickListener {
            sendNotification()
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun signOutDialog() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.activity_custom_dialog)
        val dialogText = dialog.window!!.findViewById<TextView>(R.id.textViewDialog)
        dialogText.text = "Do you want to sign out?"
        val btnYes = dialog.window!!.findViewById<Button>(R.id.buttonYes)
        val btnNo = dialog.window!!.findViewById<Button>(R.id.buttonNo)
        btnYes.setOnClickListener {
            signOut()
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}