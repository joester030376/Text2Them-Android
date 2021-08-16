package com.app.text2them.models.MSGQueueModel

data class Data(
    val AdminId: Int,
    val CampaignGUID: String,
    val CampaignId: Int,
    val CampaignName: String,
    val CommunicationGUID: String,
    val ComuncationId: Int,
    val CustomerId: Int,
    val Fullname: Any,
    val MessageContent: String,
    val MessageDate: String,
    val ReplyFrom: String,
    val Keyword: String,
    val SupportId: Int,
    val Token: Any,
    val Type: Int,
    val UserID: Int
)