package com.example.appdev_login

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val role: String = "guest"
)
