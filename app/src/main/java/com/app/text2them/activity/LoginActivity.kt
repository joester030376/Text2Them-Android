package com.app.text2them.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.text2them.R
import com.app.text2them.models.LoginModel.LoginParam
import com.app.text2them.models.LoginModel.LoginResponse
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.CustomProgressDialog
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.activity_email.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edtEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class LoginActivity : AppCompatActivity() {

    private var customProgressDialog: CustomProgressDialog? = null
    private var passwordNotVisible = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            btnLogin.setOnClickListener {
                if (validation()) {
                    loginApi()
                }
            }
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this, EmailActivity::class.java)
            startActivity(intent)
        }

        imgPasswordHide.setOnClickListener {
            if (AppUtils.getText(edtPassword).isNotEmpty()) {
                if (passwordNotVisible == 1) {
                    edtPassword!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    passwordNotVisible = 0
                    imgPasswordHide!!.setBackgroundResource(R.drawable.ic_eye_open)
                } else {
                    edtPassword!!.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordNotVisible = 1
                    imgPasswordHide!!.setBackgroundResource(R.drawable.ic_close_eyes_signup)
                }
                edtPassword!!.setSelection(edtPassword!!.length())
            }
        }
    }

    private fun validation(): Boolean {
        if (TextUtils.isEmpty(edtEmail.text.toString())) {
            //Toast.makeText(applicationContext, "Please enter email", Toast.LENGTH_SHORT).show()
            edtEmail.error = "Please enter email"
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString())
                .matches()
        ) {
//            Toast.makeText(applicationContext, "Please enter valid email", Toast.LENGTH_SHORT)
//                .show()
            edtEmail.error = "Please enter valid email"
            return false
        } else if (TextUtils.isEmpty(edtPassword.text.toString())) {
//            Toast.makeText(applicationContext, "Please enter password", Toast.LENGTH_SHORT)
//                .show()
            edtPassword.error = "Please enter password"
            return false
        } else {
            return true
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

    private fun loginApi() {
        if (AppUtils.isConnectedToInternet(this@LoginActivity)) {
            showProgressDialog(this)

            val loginParam = LoginParam(AppUtils.getText(edtEmail), AppUtils.getText(edtPassword))

            val call: Call<LoginResponse?>? = RetrofitRestClient.getInstance()?.loginApi(loginParam)

            call?.enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    hideProgressDialog()
                    val loginResponse: LoginResponse
                    if (response.isSuccessful) {
                        loginResponse = response.body()!!
                        if (loginResponse.Status) {
                            AppUtils.showToast(this@LoginActivity, loginResponse.Message)

                            MySharedPreferences.getMySharedPreferences()!!.isLogin = true
                            MySharedPreferences.getMySharedPreferences()!!.userId =
                                loginResponse.Data.Id.toString()
                            MySharedPreferences.getMySharedPreferences()!!.adminId =
                                loginResponse.Data.AdminId.toString()
                            MySharedPreferences.getMySharedPreferences()!!.userName =
                                loginResponse.Data.UserName
                            MySharedPreferences.getMySharedPreferences()!!.email =
                                loginResponse.Data.Email
                            MySharedPreferences.getMySharedPreferences()!!.accessToken =
                                loginResponse.Data.Token
                            MySharedPreferences.getMySharedPreferences()!!.password =
                                loginResponse.Data.Password
                            MySharedPreferences.getMySharedPreferences()!!.loginType =
                                loginResponse.Data.type.toString()
                            MySharedPreferences.getMySharedPreferences()!!.userImage =
                                loginResponse.Data.ProfileImage

                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            AppUtils.showToast(this@LoginActivity, loginResponse.Message)
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    hideProgressDialog()
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.connection_timeout),
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        t.printStackTrace()
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } else {
            Toast.makeText(
                this@LoginActivity,
                getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}