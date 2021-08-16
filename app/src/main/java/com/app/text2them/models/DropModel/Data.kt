package com.app.text2them.models.DropModel

data class Data(
    val AdminId: Int,
    val ContactGroup: Int,
    val DepartmentList: List<Department>,
    val Departmentid: Int,
    val DesignationList: List<Designation>,
    val Designationid: Int,
    val Token: Any,
    val Type: Int,
    val UserID: Int,
    val UserType: Int,
    val contactgrpList: List<Contactgrp>
)