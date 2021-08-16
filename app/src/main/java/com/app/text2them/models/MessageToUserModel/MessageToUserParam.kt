package com.app.text2them.models.MessageToUserModel

data class MessageToUserParam(
    val AdminId: Int,
    val ContactGroup: Int,
    val Departmentid: Int,
    val Designationid: Int,
    val Token: String,
    val Type: Int,
    val UserID: Int,
    val UserType: Int
)