package com.app.text2them.models.EditProfileModel

data class ProfileDetailsParam(
    val Email: String,
    val LastName: String,
    val Name: String,
    val Password: String,
    val Token: String,
    val Type: Int,
    val UserID: Int
)