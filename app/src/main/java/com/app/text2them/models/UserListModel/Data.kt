package com.app.text2them.models.UserListModel

data class Data(
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val staffmemberList: ArrayList<Staffmember>
)