package com.app.text2them.models.StateModel

data class Data(
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val stateList: List<State>
)