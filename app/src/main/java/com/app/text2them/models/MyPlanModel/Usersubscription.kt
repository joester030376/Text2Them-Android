package com.app.text2them.models.MyPlanModel

data class Usersubscription(
    val AdminId: Int,
    val ContactRangeEnd: Int,
    val ContactRangeStart: Int,
    val PlanName: String,
    val SubscriptionEndDate: String,
    val SubscriptionRangeId: Int,
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val UserId: Int
)