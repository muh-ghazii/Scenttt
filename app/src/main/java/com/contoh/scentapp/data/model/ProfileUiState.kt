package com.contoh.scentapp.data.model

data class ProfileUiState(
    val name       : String  = "Nama Kamu",
    val email      : String  = "nafisnervers@gmail.com",
    val isDarkMode : Boolean = true,
    val language   : String  = "INDONESIA",
    val showDeleteDialog : Boolean = false
)