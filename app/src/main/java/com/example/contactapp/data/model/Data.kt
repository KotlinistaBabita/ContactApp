package com.example.contactapp.data.model

import java.io.Serializable


data class Data(

	val date: String,
	val totalUsers: Int,
	val users: List<UsersItem?>?
):Serializable