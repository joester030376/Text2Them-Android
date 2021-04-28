package com.app.text2them.models.GetProfile

import java.io.Serializable

data class Data(
    val Country: Int,
    val Datapurge: String,
    val Description: String,
    val Email: String,
    val ID: Int,
    val LastName: String,
    val Name: String,
    val OrganizationId: Int,
    val OrganizationName: String,
    val Password: String,
    val ProfileImage: String,
    val Token: String,
    val Type: Int,
    val UserID: Int,
    val ZipCode: String,
    val CountryName: String,
    val StateName: String,
    val city: String,
    val cityList: Any,
    val countryList: Any,
    val msg: String,
    val state: Int,
    val stateList: Any,
    val website: String
) : Serializable