package com.app.text2them.activity

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.text2them.R
import com.app.text2them.models.ForgotPassword.ForgotPassParam
import com.app.text2them.models.ForgotPassword.ForgotPassRes
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.CustomProgressDialog
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.activity_email.*
import kotlinx.android.synthetic.main.activity_email.edtEmail
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class EmailActivity : AppCompatActivity() {

    private var customProgressDialog: CustomProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        ivBack.setOnClickListener {
            finish()
        }

        btnEmail.setOnClickListener {
            if (validation()) {
                forgotPassword()
            }
        }
    }

    private fun validation(): Boolean {
        return if (TextUtils.isEmpty(edtEmail.text.toString())) {
            //Toast.makeText(applicationContext, "Please enter email", Toast.LENGTH_SHORT).show()
            edtEmail.error = "Please enter email"
            false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString())
                .matches()
        ) {
            edtEmail.error = "Please enter valid email"
    //            Toast.makeText(applicationContext, "Please enter valid email", Toast.LENGTH_SHORT)
    //                .show()
            false
        } else {
            true
        }
    }

    private fun forgotPassword() {
        if (AppUtils.isConnectedToInternet(this@EmailActivity)) {
            showProgressDialog(this)
            val forgotPassParam = ForgotPassParam(AppUtils.getText(edtEmail))

            val call: Call<ForgotPassRes?>? =
                RetrofitRestClient.getInstance()?.forgotPasswordApi(forgotPassParam)

            call?.enqueue(object : Callback<ForgotPassRes?> {
                override fun onResponse(
                    call: Call<ForgotPassRes?>,
                    response: Response<ForgotPassRes?>
                ) {
                    hideProgressDialog()
                    val passwordResponse: ForgotPassRes = response.body()!!
                    if (response.isSuccessful) {
                        if (passwordResponse.Status) {
                            AppUtils.showToast(this@EmailActivity, passwordResponse.Message)
                            finish()
                        } else {
                            AppUtils.showToast(this@EmailActivity, passwordResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(this@EmailActivity, response.message())
                    }
                }

                override fun onFailure(call: Call<ForgotPassRes?>, t: Throwable) {
                    hideProgressDialog()
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            this@EmailActivity,
                            getString(R.string.connection_timeout),
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        t.printStackTrace()
                        Toast.makeText(
                            this@EmailActivity,
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } else {
            Toast.makeText(
                this@EmailActivity,
                getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun showProgressDialog(ctx: Context) {
        try {
            customProgressDialog = CustomProgressDialog(ctx)
            customProgressDialog!!.show()
            customProgressDialog!!.setCancelable(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideProgressDialog() {
        try {
            if (customProgressDialog != null && customProgressDialog!!.isShowing) {
                customProgressDialog!!.dismiss()
                customProgressDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}