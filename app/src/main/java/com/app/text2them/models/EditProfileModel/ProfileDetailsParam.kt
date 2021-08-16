package com.app.text2them.models.EditProfileModel

data class ProfileDetailsParam(
    val Email: String,
    val LastName: String,
    val Name: String,
    val Password: String,
    val Token: String,
    val Type: Int,
    val UserID: Int,
    val Country: Int,
    val Datapurge: String,
    val Description: String,
    val OrganizationId: Int,
    val OrganizationName: String,
    val ZipCode: String,
    val city: String,
    val state: Int,
    val website: String,
)