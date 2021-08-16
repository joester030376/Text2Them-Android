package com.app.text2them.models.ChatModel

data class Chat(
    val AdminId: Int,
    val CampaignGUID: String,
    val CampaignId: Int,
    val CommunicationGUID: String,
    val CommunicationId: Int,
    val CreatedBy: Int,
    val FirstName: Any,
    val LastName: Any,
    val Message: Any,
    val MessageContent: String,
    val MessageDate: String,
    val ReplyFrom: String,
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val chatList: Any,
    val skipcount: Int,
    val takecount: Int
)