package com.app.text2them.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.text2them.R
import com.app.text2them.models.SendMessage.SendMessageParam
import com.app.text2them.models.SendMessage.SendMessageRes
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_send_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SendMessageFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_send_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSend.setOnClickListener {
            if (validation()) {
                sendNewMessage()
            }
        }
    }

    private fun validation(): Boolean {
        when {
            AppUtils.isEmpty(edtFName.text.toString()) -> {
                edtFName.error = "Please enter first name"
                return false
            }
            AppUtils.isEmpty(edtLName.text.toString()) -> {
                edtLName.error = "Please enter last name"
                return false
            }
            AppUtils.isEmpty(edtNumber.text.toString()) -> {
                edtNumber.error = "Please enter mobile number"
                return false
            }
            AppUtils.getText(edtNumber).length < 10 -> {
                edtNumber.error = "Please enter valid mobile number"
                return false
            }
            AppUtils.isEmpty(edtLMessage.text.toString()) -> {
                edtLMessage.error = "Please enter message"
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun sendNewMessage() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val param = SendMessageParam(
                edtFName.text.toString(),
                edtLName.text.toString(),
                edtNumber.text.toString(),
                edtLMessage.text.toString(),
                MySharedPreferences.getMySharedPreferences()?.accessToken!!,
                MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
            )

            val call: Call<SendMessageRes?>? =
                RetrofitRestClient.getInstance()?.sendNewMessage(param)

            call?.enqueue(object : Callback<SendMessageRes?> {
                override fun onResponse(
                    call: Call<SendMessageRes?>,
                    response: Response<SendMessageRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val editUserResponse: SendMessageRes = response.body()!!
                        if (editUserResponse.Status) {
                            edtFName.setText("")
                            edtLName.setText("")
                            edtNumber.setText("")
                            edtLMessage.setText("")
                            AppUtils.showToast(requireActivity(), editUserResponse.Message)
                        } else {
                            AppUtils.showToast(requireActivity(), editUserResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<SendMessageRes?>, t: Throwable) {
                    hideProgressDialog()
                    if (t is SocketTimeoutException) {
                        AppUtils.showToast(
                            requireActivity(),
                            getString(R.string.connection_timeout)
                        )
                    } else {
                        t.printStackTrace()
                        AppUtils.showToast(
                            requireActivity(),
                            getString(R.string.something_went_wrong)
                        )
                    }
                }
            })
        } else {
            AppUtils.showToast(
                requireActivity(), getString(R.string.no_internet)
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SendMessageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}