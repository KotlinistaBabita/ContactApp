package com.example.contactapp.data.network

import com.example.contactapp.data.model.ContactResponse


class ContactRepository(private val apiService: ContactApi) {

    suspend fun fetchTodayContacts(): Result<ContactResponse> {
        return try {
            val response = apiService.getTodayContacts()
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}