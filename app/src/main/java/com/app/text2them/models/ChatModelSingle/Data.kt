package com.app.text2them.models.ChatModelSingle

data class Data(
    val AdminId: Int,
    val MarketingCampaignGUID: Any,
    val SenderNetworkEmailId: Any,
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val sendmessageList: ArrayList<Sendmessage>
)