package com.app.text2them.models.MyPlanModel

data class Data(
    val AdminId: Int,
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val usersubscriptionList: ArrayList<Usersubscription>
)