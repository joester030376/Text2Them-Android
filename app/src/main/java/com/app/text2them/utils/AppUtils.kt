package com.app.text2them.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.text2them.R
import com.app.text2them.Text2ThemApp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.Field
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

object AppUtils {
    private const val APP_TAG = "Text2Them"
    var d: String = ""

    fun logString(message: String?): Int {
        return Log.i(APP_TAG, message!!)
    }

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isZipCodeValid(zipCode: String?): Boolean {
        val expression = "[0-9]{5}(?:-[0-9]{4})?\$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(zipCode)
        return matcher.matches()
    }

    fun isNameValid(name: String?): Boolean {
        val expression = "^[a-zA-Z]+\$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(name)
        return matcher.matches()
    }

    fun isNumericNameValid(name: String?): Boolean {
        val expression = "^[a-zA-Z0-9]*$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(name)
        return matcher.matches()
    }

    @JvmStatic
    fun isValidPassword(password: String?): Boolean {
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }


    fun isPhoneValid(phone: String?): Boolean {
        val expression = "^+(?:[0-9] ?){6,16}[0-9]\$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(phone)
        return matcher.matches()
    }


    @JvmStatic
    fun getText(textView: TextView): String {
        return textView.text.toString().trim { it <= ' ' }
    }

    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /*   public static RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }*/
    @JvmStatic
    fun hideSoftKeyboard(activity: Activity) {
        val focusedView = activity.currentFocus
        if (focusedView != null) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

    // Check EditText or String is Empty or null etc.
    fun isEmpty(str: String?): Boolean {
        return TextUtils.isEmpty(str)
    }


    fun showAlertDialog(context: Context, title: String?, message: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(context.getString(R.string.ok)) { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    @JvmStatic
    fun roundTwoDecimals(d: Double): Double {
        val twoDForm = DecimalFormat("#.##")
        return java.lang.Double.valueOf(twoDForm.format(d))
    }

    @JvmStatic
    fun isMyServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val manager = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun capitalizeText(capString: String): String? {
        val capBuffer = StringBuffer()
        val capMatcher: Matcher =
            Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(
                capBuffer,
                capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase()
            )
        }
        return capMatcher.appendTail(capBuffer).toString()
    }

    @JvmStatic
    fun dateChange(time: String): String? {
        val inputPattern = "dd-MM-yyyy HH:mm"
        val outputPattern = "HH:mm"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }


    fun getRequestBody(value: String?): RequestBody? {
        return value!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    @JvmStatic
    fun isConnectedToInternet(context: Context?): Boolean {
        val cm =
            Text2ThemApp.text2ThemApp!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var netInfo: NetworkInfo? = null
        if (cm != null) {
            netInfo = cm.activeNetworkInfo
        }
        return netInfo != null && netInfo.isConnected && netInfo.isAvailable
    }

    fun priceFormatter(price: Double?): String {
        val formatter: NumberFormat = DecimalFormat("#0.000")
        return formatter.format(price)
    }

    fun covertTimeToText(dataDate: String?): String? {
        var convTime: String? = null
        val suffix = "Ago"
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val pasTime: Date? = dateFormat.parse(dataDate!!)
            val nowTime = Date()
            val dateDiff: Long = nowTime.getTime() - pasTime!!.getTime()
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second Seconds $suffix"
            } else if (minute < 60) {
                convTime = "$minute Minutes $suffix"
            } else if (hour < 24) {
                convTime = "$hour Hours $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            } else if (day < 7) {
                convTime = "$day Days $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        //Log.e("--->ConvertTime", convTime.toString())
        return convTime
    }

    fun getCapsSentences(tagName: String?): String? {
        val splits = tagName!!.toLowerCase().split(" ".toRegex()).toTypedArray()
        val sb = StringBuilder()
        for (i in splits.indices) {
            val eachWord = splits[i]
            if (i > 0 && eachWord.length > 0) {
                sb.append(" ")
            }
            val cap = (eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1))
            sb.append(cap)
        }
        return sb.toString()
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr: String = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    /** Returns the consumer friendly device name  */
    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }

    fun getOSVersion(): String {
        val builder = java.lang.StringBuilder()
        builder.append("android : ").append(Build.VERSION.RELEASE)

        val fields: Array<Field> = VERSION_CODES::class.java.fields
        for (field in fields) {
            val fieldName: String = field.name
            var fieldValue = -1
            try {
                fieldValue = field.getInt(Any())
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ")
                builder.append("sdk=").append(fieldValue)
            }
        }
        return builder.toString()
    }

    fun getAndroidVersion(sdk: Int): String? {
        return when (sdk) {
            1 -> "(Android 1.0)"
            2 -> "Petit Four" + "(Android 1.1)"
            3 -> "Cupcake" + "(Android 1.5)"
            4 -> "Donut" + "(Android 1.6)"
            5 -> "Eclair" + "(Android 2.0)"
            6 -> "Eclair" + "(Android 2.0.1)"
            7 -> "Eclair" + "(Android 2.1)"
            8 -> "Froyo" + "(Android 2.2)"
            9 -> "Gingerbread" + "(Android 2.3)"
            10 -> "Gingerbread" + "(Android 2.3.3)"
            11 -> "Honeycomb" + "(Android 3.0)"
            12 -> "Honeycomb" + "(Android 3.1)"
            13 -> "Honeycomb" + "(Android 3.2)"
            14 -> "Ice Cream Sandwich" + "(Android 4.0)"
            15 -> "Ice Cream Sandwich" + "(Android 4.0.3)"
            16 -> "Jelly Bean" + "(Android 4.1)"
            17 -> "Jelly Bean" + "(Android 4.2)"
            18 -> "Jelly Bean" + "(Android 4.3)"
            19 -> "KitKat" + "(Android 4.4)"
            20 -> "KitKat Watch" + "(Android 4.4)"
            21 -> "Lollipop" + "(Android 5.0)"
            22 -> "Lollipop" + "(Android 5.1)"
            23 -> "Marshmallow" + "(Android 6.0)"
            24 -> "Nougat" + "(Android 7.0)"
            25 -> "Nougat" + "(Android 7.1.1)"
            26 -> "Oreo" + "(Android 8.0)"
            27 -> "Oreo" + "(Android 8.1)"
            28 -> "Pie" + "(Android 9.0)"
            29 -> "Q" + "(Android 10.0)"
            30 -> "Android 11" + ""
            else -> ""
        }
    }

    fun getProgressDisplayLine(currentBytes: Long, totalBytes: Long): String? {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
    }

    fun getBytesToMBString(bytes: Long): String {
        return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }

    fun getRootDirPath(context: Context): String? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }
}