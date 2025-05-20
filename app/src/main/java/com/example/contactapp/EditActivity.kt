package com.example.contactapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etCompany: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button

    private var userId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_contact)

        etFirstName = findViewById(R.id.etFirstName)
        etSurname = findViewById(R.id.etSurname)
        etCompany = findViewById(R.id.etCompany)
        etPhone = findViewById(R.id.etPhone)
        btnSave = findViewById(R.id.btnSave)

        val fullName = intent.getStringExtra("fullName") ?: ""
        val course = intent.getStringExtra("course") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        userId = intent.getIntExtra("userId", -1)

        val nameParts = fullName.split(" ")
        val firstName = nameParts.getOrNull(0) ?: ""
        val surname = nameParts.getOrNull(1) ?: ""
        etFirstName.setText(firstName)
        etSurname.setText(surname)
        etCompany.setText(course)
        etPhone.setText(phone)

        btnSave.setOnClickListener {
            val updatedFirstName = etFirstName.text.toString().trim()
            val updatedSurname = etSurname.text.toString().trim()
            val updatedCompany = etCompany.text.toString().trim()
            val updatedPhone = etPhone.text.toString().trim()

            if (updatedFirstName.isEmpty() || updatedPhone.isEmpty()) {
                Toast.makeText(this, "First name and phone are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("userId", userId)
                putExtra("fullName", "$updatedFirstName $updatedSurname")
                putExtra("course", updatedCompany)
                putExtra("phone", updatedPhone)
            }

            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }

}