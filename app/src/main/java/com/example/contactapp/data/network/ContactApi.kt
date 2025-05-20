package com.example.contactapp.data.network

import com.example.contactapp.data.model.ContactResponse
import retrofit2.http.GET

interface ContactApi {

    @GET("/api/contacts")
    suspend fun getTodayContacts(): ContactResponse

}