package com.app.text2them.models.ChatModel

data class Data(
    val AdminId: Int,
    val CampaignGUID: String,
    val CampaignId: Int,
    val CommunicationGUID: String,
    val CommunicationId: Int,
    val CreatedBy: Int,
    val FirstName: String,
    val LastName: String,
    val Message: Any,
    val MessageContent: Any,
    val MessageDate: String,
    val ReplyFrom: String,
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val chatList: ArrayList<Chat>,
    val skipcount: Int,
    val takecount: Int
)