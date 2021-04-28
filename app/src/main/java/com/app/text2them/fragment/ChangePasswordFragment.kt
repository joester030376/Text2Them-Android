package com.app.text2them.fragment

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.text2them.R
import com.app.text2them.activity.LoginActivity
import com.app.text2them.models.ChnagePassword.ChangePassParam
import com.app.text2them.models.ChnagePassword.ChangePassResponse
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChangePasswordFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var passwordNotVisibleOld = 1
    private var passwordNotVisibleNew = 1
    private var passwordNotVisibleCNew = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmit.setOnClickListener {
            if (validationChangePassword()) {
                changePassword()
            }
        }

        imgPasswordHideOld.setOnClickListener {
            if (AppUtils.getText(edtOldPass).isNotEmpty()) {
                if (passwordNotVisibleOld == 1) {
                    edtOldPass!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    passwordNotVisibleOld = 0
                    imgPasswordHideOld!!.setBackgroundResource(R.drawable.ic_eye_open)
                } else {
                    edtOldPass!!.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordNotVisibleOld = 1
                    imgPasswordHideOld!!.setBackgroundResource(R.drawable.ic_close_eyes_signup)
                }
                edtOldPass!!.setSelection(edtOldPass!!.length())
            }
        }

        imgPasswordHideNew.setOnClickListener {
            if (AppUtils.getText(edtNewPass).isNotEmpty()) {
                if (passwordNotVisibleNew == 1) {
                    edtNewPass!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    passwordNotVisibleNew = 0
                    imgPasswordHideNew!!.setBackgroundResource(R.drawable.ic_eye_open)
                } else {
                    edtNewPass!!.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordNotVisibleNew = 1
                    imgPasswordHideNew!!.setBackgroundResource(R.drawable.ic_close_eyes_signup)
                }
                edtNewPass!!.setSelection(edtNewPass!!.length())
            }
        }

        imgPasswordHideCNew.setOnClickListener {
            if (AppUtils.getText(edtCNewPass).isNotEmpty()) {
                if (passwordNotVisibleCNew == 1) {
                    edtCNewPass!!.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    passwordNotVisibleCNew = 0
                    imgPasswordHideCNew!!.setBackgroundResource(R.drawable.ic_eye_open)
                } else {
                    edtCNewPass!!.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordNotVisibleCNew = 1
                    imgPasswordHideCNew!!.setBackgroundResource(R.drawable.ic_close_eyes_signup)
                }
                edtCNewPass!!.setSelection(edtCNewPass!!.length())
            }
        }
    }

    private fun validationChangePassword(): Boolean {
        return when {
            TextUtils.isEmpty(AppUtils.getText(edtOldPass)) -> {
                Toast.makeText(
                    requireActivity(),
                    "Please enter current password",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            AppUtils.getText(edtOldPass).length < 6 -> {
                Toast.makeText(
                    requireActivity(),
                    "Password length is small",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            TextUtils.isEmpty(AppUtils.getText(edtNewPass)) -> {
                Toast.makeText(
                    requireActivity(),
                    "Please enter new password",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            AppUtils.getText(edtNewPass).length < 6 -> {
                Toast.makeText(
                    requireActivity(),
                    "Password length is small",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            TextUtils.isEmpty(AppUtils.getText(edtCNewPass)) -> {
                Toast.makeText(
                    requireActivity(),
                    "Please enter confirm password",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            AppUtils.getText(edtCNewPass).length < 6 -> {
                Toast.makeText(
                    requireActivity(),
                    "Password length is small",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            edtNewPass.text.toString() != edtCNewPass.text.toString() -> {
                Toast.makeText(
                    requireActivity(),
                    "New password and confirm password does not match",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun changePassword() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val changePasswordParam =
                ChangePassParam(
                    AppUtils.getText(edtOldPass),
                    AppUtils.getText(edtNewPass),
                    AppUtils.getText(edtCNewPass),
                    MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
                )

            val call: Call<ChangePassResponse?>? =
                RetrofitRestClient.getInstance()?.changePasswordApi(changePasswordParam)

            call?.enqueue(object : Callback<ChangePassResponse?> {
                override fun onResponse(
                    call: Call<ChangePassResponse?>,
                    response: Response<ChangePassResponse?>
                ) {
                    hideProgressDialog()
                    val passwordResponse: ChangePassResponse = response.body()!!
                    if (response.isSuccessful) {
                        if (passwordResponse.Status) {
                            AppUtils.showToast(requireActivity(), passwordResponse.Message)
                            val intent =
                                Intent(
                                    requireActivity(),
                                    LoginActivity::class.java
                                )
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            requireActivity().finish()

                        } else {
                            AppUtils.showToast(requireActivity(), passwordResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<ChangePassResponse?>, t: Throwable) {
                    hideProgressDialog()
                    if (t is SocketTimeoutException) {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.connection_timeout),
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        t.printStackTrace()
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}