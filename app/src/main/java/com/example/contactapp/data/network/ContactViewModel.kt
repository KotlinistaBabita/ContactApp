package com.example.contactapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.model.UsersItem
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _contacts = MutableLiveData<List<UsersItem>>()
    val contacts: LiveData<List<UsersItem>> = _contacts
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun syncTodayContacts() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            when(val result = repository.fetchTodayContacts()) {
                is Result.Success -> {
                    val data = result.data.Data
                    _contacts.postValue(data.users?.filterNotNull() ?: emptyList())
                    _date.postValue(data.date)
                }
                is Result.Error -> {
                    Log.e("ContactViewModel", "Error fetching contacts", result.exception)
                    _contacts.postValue(emptyList())
                    _date.postValue("")                }
            }
            _isLoading.postValue(false)
        }
    }

    fun addContactManually(user: UsersItem) {
        val updatedList = _contacts.value.orEmpty().toMutableList().apply { add(user) }
        _contacts.value = updatedList
    }
}
