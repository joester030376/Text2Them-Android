package com.app.text2them.models.CountryModel

data class Data(
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val countryList: List<Country>
)