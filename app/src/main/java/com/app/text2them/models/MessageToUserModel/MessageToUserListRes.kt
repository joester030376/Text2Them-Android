package com.app.text2them.models.MessageToUserModel

data class MessageToUserListRes(
    val Data: ArrayList<UserListData>,
    val Message: String,
    val Status: Boolean,
    val strStatus: String
)