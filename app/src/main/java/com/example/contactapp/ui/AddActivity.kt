package com.example.contactapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contactapp.R
import com.example.contactapp.data.model.AddContact
import com.example.contactapp.data.model.Contact
import com.example.contactapp.utils.ContactUtils

class AddActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etCompany: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var image: ImageView
    private var selectedImageUri: Uri? = null
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact)

        etFirstName = findViewById(R.id.etFirstName)
        etSurname = findViewById(R.id.etSurname)
        etCompany = findViewById(R.id.etCompany)
        etPhone = findViewById(R.id.etPhone)
        btnSave = findViewById(R.id.btnSave)
        image = findViewById(R.id.imgAddPicture)
        image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        btnSave.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val surname = etSurname.text.toString().trim()
            val company = etCompany.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (firstName.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "First Name and Phone are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (checkSelfPermission(android.Manifest.permission.WRITE_CONTACTS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.WRITE_CONTACTS), 1001)
                return@setOnClickListener
            }

            saveContact(firstName, surname, company, phone)
        }
    }



    private fun saveContact(firstName: String, surname: String, company: String, phone: String) {
        val contact = AddContact(
            firstName = firstName,
            surname = surname,
            company = company,
            phone = phone,
            imageUri = selectedImageUri?.toString(),
        )

        val contactToSync = Contact(
            contactId = 0L,
            name = "$firstName $surname".trim(),
            phone = phone,
            email = null,
            company = company
        )

        ContactUtils.syncContactsToPhone(this, listOf(contactToSync))

        val intent = Intent().apply {
            putExtra("newContact", contact)
        }

        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            image.setImageURI(selectedImageUri)
            }
        }


}