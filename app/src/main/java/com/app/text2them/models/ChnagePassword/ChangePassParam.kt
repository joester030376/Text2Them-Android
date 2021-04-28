package com.app.text2them.models.ChnagePassword

data class ChangePassParam(
    val ConfirmPassword: String,
    val NewPassword: String,
    val OldPassword: String,
    val id: Int
)