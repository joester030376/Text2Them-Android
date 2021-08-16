package com.app.text2them.models.ChatModelSingle

data class Sendmessage(
    val AdminId: Int,
    val CampaingName: String,
    val CreatedBy: Int,
    val CreatedDate: String,
    val CreatedDateTimeStr: String,
    val FirstName: Any,
    val Id: Int,
    val LastName: Any,
    val MarketingCampaignGUID: String,
    val MarketingCampaignId: Int,
    val Message: String,
    val ModifiedDate: String,
    val ModifyedBy: Int,
    val SenderNetworkid: String
)