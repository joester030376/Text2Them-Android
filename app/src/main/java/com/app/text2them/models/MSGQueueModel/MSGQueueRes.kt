package com.app.text2them.models.MSGQueueModel

data class MSGQueueRes(
    val Data: ArrayList<Data>,
    val Message: String,
    val Status: Boolean,
    val strStatus: String
)