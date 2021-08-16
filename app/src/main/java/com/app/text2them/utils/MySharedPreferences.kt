package com.app.text2them.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.app.text2them.Text2ThemApp.Companion.text2ThemApp
import com.bmd.mybmd.api.UrlConstant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@SuppressLint("CommitPrefEdits")
class MySharedPreferences private constructor(context: Context?, gson: Gson) {
    private val SP_NAME = "T2T_Prefs"

    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    var gson: Gson

    var firebaseToken: String?
        get() = sharedPreferences.getString(UrlConstant.FIREBASE_TOKEN, "test")
        set(firebaseToken) {
            editor.putString(UrlConstant.FIREBASE_TOKEN, firebaseToken).apply()
        }

    var deviceID: String?
        get() = sharedPreferences.getString(UrlConstant.USER_DEVICE_ID, "")
        set(deviceId) {
            editor.putString(UrlConstant.USER_DEVICE_ID, deviceId).apply()
        }

    var isLogin: Boolean
        get() = sharedPreferences.getBoolean(UrlConstant.IS_LOGIN, false)
        set(isLogin) {
            editor.putBoolean(UrlConstant.IS_LOGIN, isLogin).apply()
        }

    var notificationStatus: String?
        get() = sharedPreferences.getString(UrlConstant.NOTIFICATION, "N")
        set(notificationStatus) {
            editor.putString(UrlConstant.NOTIFICATION, notificationStatus).apply()
        }

    var address: String?
        get() = sharedPreferences.getString(UrlConstant.ADDRESS, "N")
        set(address) {
            editor.putString(UrlConstant.ADDRESS, address).apply()
        }

    var latitude: String?
        get() = sharedPreferences.getString(UrlConstant.LATITUDE, "")
        set(latitude) {
            editor.putString(UrlConstant.LATITUDE, latitude).apply()
        }

    var longitude: String?
        get() = sharedPreferences.getString(UrlConstant.LONGITUDE, "")
        set(longitude) {
            editor.putString(UrlConstant.LONGITUDE, longitude).apply()
        }

    var loginType: String?
        get() = sharedPreferences.getString(UrlConstant.LOGIN_TYPE, "")
        set(countryCode) {
            editor.putString(UrlConstant.LOGIN_TYPE, countryCode).apply()
        }

    var accessToken: String?
        get() = sharedPreferences.getString(UrlConstant.ACCESS_TOKEN, "")
        set(accessToken) {
            editor.putString(UrlConstant.ACCESS_TOKEN, accessToken).apply()
        }

    var userName: String?
        get() = sharedPreferences.getString(UrlConstant.USER_NAME, "")
        set(userName) {
            editor.putString(UrlConstant.USER_NAME, userName).apply()
        }

    var userId: String?
        get() = sharedPreferences.getString(UrlConstant.USER_ID, "")
        set(userId) {
            editor.putString(UrlConstant.USER_ID, userId).apply()
        }

    var adminId: String?
        get() = sharedPreferences.getString(UrlConstant.ADMIN_ID, "")
        set(adminId) {
            editor.putString(UrlConstant.ADMIN_ID, adminId).apply()
        }

    var fullName: String?
        get() = sharedPreferences.getString(UrlConstant.USER_FULL_NAME, "")
        set(fullName) {
            editor.putString(UrlConstant.USER_FULL_NAME, fullName).apply()
        }

    var email: String?
        get() = sharedPreferences.getString(UrlConstant.USER_EMAIL, "")
        set(email) {
            editor.putString(UrlConstant.USER_EMAIL, email).apply()
        }

    var password: String?
        get() = sharedPreferences.getString(UrlConstant.PASSWORD, "")
        set(password) {
            editor.putString(UrlConstant.PASSWORD, password).apply()
        }

    var contactNumber: String?
        get() = sharedPreferences.getString(UrlConstant.USER_CONTACT_NUMBER, "")
        set(contactNumber) {
            editor.putString(UrlConstant.USER_CONTACT_NUMBER, contactNumber).apply()
        }

    var countryName: String?
        get() = sharedPreferences.getString(UrlConstant.USER_COUNTRY_NAME, "")
        set(countryName) {
            editor.putString(UrlConstant.USER_COUNTRY_NAME, countryName).apply()
        }

    var userImage: String?
        get() = sharedPreferences.getString(UrlConstant.USER_IMAGE, "")
        set(userImage) {
            editor.putString(UrlConstant.USER_IMAGE, userImage).apply()
        }

    var city: String?
        get() = sharedPreferences.getString(UrlConstant.CITY, "")
        set(city) {
            editor.putString(UrlConstant.CITY, city).apply()
        }

    fun clearPreferences() {
        editor.clear().apply()
    }

    fun <T> putObject(key: String?, value: T) {
        val editor = sharedPreferences.edit()
        editor.putString(key, gson.toJson(value))
        editor.apply()
    }

    fun <T> getObject(key: String?, clazz: Class<T>?): T {
        return gson.fromJson(getString(key, null), clazz)
    }

    fun <T> putList(key: String?, list: List<T>?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, gson.toJson(list))
        editor.apply()
    }

    fun <T> getList(key: String?, clazz: Class<T>?): List<T> {
        val typeOfT = TypeToken.getParameterized(MutableList::class.java, clazz).type
        return gson.fromJson(getString(key, null), typeOfT)
    }

    fun <T> putArray(key: String?, arrays: Array<T>?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, gson.toJson(arrays))
        editor.apply()
    }

    fun <T> getArray(key: String?, clazz: Class<Array<T>?>?): Array<T> {
        return gson.fromJson(getString(key, null), clazz)!!
    }

    fun removeKey(key: String?) {
        val editor = sharedPreferences.edit()
        if (editor != null) {
            editor.remove(key)
            editor.apply()
        }
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    companion object {
        private var mySharedPreferences: MySharedPreferences? = null

        @JvmName("getMySharedPreferences1")
        fun getMySharedPreferences(): MySharedPreferences? {
            if (mySharedPreferences == null) {
                mySharedPreferences = MySharedPreferences(text2ThemApp, Gson())
            }
            return mySharedPreferences
        }
    }

    init {
        sharedPreferences = context!!.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        this.gson = gson
    }
}