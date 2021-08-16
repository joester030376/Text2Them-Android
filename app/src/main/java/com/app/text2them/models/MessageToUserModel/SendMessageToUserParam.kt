package com.app.text2them.models.MessageToUserModel

data class SendMessageToUserParam(
    val AdminId: Int,
    val MeesageTouserList: List<SelectedUser>,
    val MessageText: String,
    val Subject: String,
    val Token: String,
    val Type: Int,
    val UserID: Int
)