package com.example.contactapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.R
import com.example.contactapp.adapter.ContactAdapter
import com.example.contactapp.data.model.AddContact
import com.example.contactapp.data.model.Contact
import com.example.contactapp.utils.ContactUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var btnSyncContacts: FloatingActionButton
    private lateinit var btnAddContacts: FloatingActionButton
    val EDIT_CONTACT_REQUEST_CODE = 100
    private val ADD_CONTACT_REQUEST_CODE = 102
    private var contactList = mutableListOf<Contact>()
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 101
    private val mainScope = MainScope()
    private lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnSyncContacts= findViewById(R.id.fabSyncContacts)
        btnAddContacts= findViewById(R.id.fabAddContact)
        progressBar= findViewById(R.id.progressBarLoading)


        if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            loadContacts()
        }
        btnSyncContacts.setOnClickListener {
            syncContactsFromApi()
        }

        btnAddContacts.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, ADD_CONTACT_REQUEST_CODE)


        }



    }
    private fun syncContactsFromApi() {
        btnSyncContacts.isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "New Contacts Found", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, NewContact::class.java)
            startActivityForResult(intent, EDIT_CONTACT_REQUEST_CODE)

            btnSyncContacts.isEnabled = true
        }, 2000)
    }

    private fun loadContacts() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        mainScope.launch {
            val contacts = withContext(Dispatchers.IO) {
                ContactUtils.getDeviceContacts(this@MainActivity)
            }

            contactList.clear()
            contactList.addAll(contacts)

            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            contactAdapter = ContactAdapter(contactList)
            recyclerView.adapter = contactAdapter
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                Toast.makeText(this, "Permission required to read contacts", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_CONTACT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val updatedUserId = data.getIntExtra("userId", -1)
            val updatedFullName = data.getStringExtra("fullName") ?: ""
            val updatedCourse = data.getStringExtra("course") ?: ""
            val updatedPhone = data.getStringExtra("phone") ?: ""

            val index = contactList.indexOfFirst { it.contactId.toInt() == updatedUserId }
            if (index != -1) {
                val updatedContact = contactList[index].copy(
                    name = updatedFullName,
                    company = updatedCourse,
                    phone = updatedPhone
                )
                contactList[index] = updatedContact
                contactAdapter.notifyItemChanged(index)
            }
        }

        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
            loadContacts()
        }
    }


}