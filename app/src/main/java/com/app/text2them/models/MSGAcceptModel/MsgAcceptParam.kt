package com.app.text2them.models.MSGAcceptModel

data class MsgAcceptParam(
    val AdminId: Int,
    val CampaignGUID: String,
    val ReplyFrom: String,
    val Token: String,
    val Type: Int,
    val UserID: Int
)