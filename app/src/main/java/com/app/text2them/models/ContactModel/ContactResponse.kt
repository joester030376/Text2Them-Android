package com.app.text2them.models.ContactModel

data class ContactResponse(
    val Data: List<Data>,
    val Message: String,
    val Status: Boolean,
    val strStatus: String
)