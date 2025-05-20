package com.example.contactapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddContact(
    var firstName: String = "",
    var surname: String = "",
    var company: String = "",
    var phone: String = "",
    var imageUri: String?,
):Parcelable
