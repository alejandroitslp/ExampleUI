package com.peraz.exampleui.data.remote

data class UserModel(
    val admin: Int,
    val created_at: String,
    val deleted_at: Any,
    val email: String,
    val email_verified_at: Any,
    val firstname: String,
    val fullName: String,
    val token: String,
    val id: Int,
    val lastname: String,
    val updated_at: String
)