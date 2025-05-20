package com.example.contactapp.data.model

data class Contact(
    val contactId: Long,
    val name: String,
    val phone: String,
    val email: String?,
    val company: String?
    )
