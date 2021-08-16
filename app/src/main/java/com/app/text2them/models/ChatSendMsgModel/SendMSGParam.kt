package com.app.text2them.models.ChatSendMsgModel

data class SendMSGParam(
    val CampaignGUID: String,
    val CampaignId: Int,
    val CommunicationGUID: String,
    val Message: String,
    val ReplyFrom: String,
    val UserID: Int
)