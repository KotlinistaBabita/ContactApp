package com.example.contactapp.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.data.network.ContactRepository
import com.example.contactapp.data.network.ContactViewModel
import com.example.contactapp.data.network.ContactViewModelFactory
import com.example.contactapp.R
import com.example.contactapp.adapter.NewContactAdapter
import com.example.contactapp.data.model.Contact
import com.example.contactapp.data.network.RetroFitClient
import com.example.contactapp.utils.ContactUtils

class NewContact : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: NewContactAdapter
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var lastSync: TextView
    private lateinit var btnSync: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.new_contact)
        val apiService = RetroFitClient.api
        val repository = ContactRepository(apiService)
        val factory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]
        recyclerView = findViewById(R.id.recyclerViewContacts)
        progressBar = findViewById(R.id.progressBarNewContacts)
        lastSync = findViewById(R.id.tvLastSyncedDate)
        btnSync = findViewById(R.id.btnSyncContacts)

        recyclerView.layoutManager = LinearLayoutManager(this)

        contactViewModel.syncTodayContacts()
        btnSync.setOnClickListener {
            val newContacts: List<Contact>? = contactViewModel.contacts.value?.map { userItem ->
                Contact(
                    contactId = 0L,
                    name = userItem.fullName!!,
                    phone = userItem.phone ?: "",
                    email = userItem.email,
                    company = userItem.course
                )
            }
            ContactUtils.syncContactsToPhone(this, newContacts!!)
            showCustomSyncSuccessPopup(this)

        }

        contactViewModel.contacts.observe(this) { contacts ->
            adapter = NewContactAdapter(contacts, this)
            recyclerView.adapter = adapter


        }
        contactViewModel.date.observe(this){date ->
            lastSync.text= date

        }

        contactViewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        }

    }

    fun showCustomSyncSuccessPopup(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.success)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //val tvMessage = dialog.findViewById<TextView>(R.id.tvMessage)
        //tvMessage.text = "Contacts synced successfully."

        dialog.show()

        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()

            // Navigate to ContactListActivity
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)

            if (context is Activity) {
                context.finish()
            }

        }, 2000) // 2 seconds
    }



}