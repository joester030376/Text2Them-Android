package com.app.text2them.models.SendMessage

data class SendMessageParam(
    val NewMessageFirstName: String,
    val NewMessageLastName: String,
    val NewMessageMobileNo: String,
    val NewMessageText: String,
    val Token: String,
    val Type: Int,
    val UserID: Int
)