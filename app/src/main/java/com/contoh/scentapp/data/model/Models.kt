package com.contoh.scentapp.data.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val defaultAddress: String = "",
    val scentProfile: List<String> = emptyList()
)