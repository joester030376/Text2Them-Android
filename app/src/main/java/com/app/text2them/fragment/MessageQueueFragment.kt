package com.app.text2them.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.text2them.R
import com.app.text2them.adapter.MSGQueueAdapter
import com.app.text2them.models.MSGAcceptModel.MsgAcceptParam
import com.app.text2them.models.MSGAcceptModel.MsgAcceptRes
import com.app.text2them.models.MSGQueueModel.Data
import com.app.text2them.models.MSGQueueModel.MSGQueueRes
import com.app.text2them.models.MSGQueueModel.MessageQueueParam
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_message_queue.*
import kotlinx.android.synthetic.main.fragment_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MessageQueueFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var msgQueueAdapter: MSGQueueAdapter
    var msgQueueList = ArrayList<Data>()

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
        return inflater.inflate(R.layout.fragment_message_queue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleMSGQueue.layoutManager = LinearLayoutManager(requireActivity())
        getMSGQueueListApi()
    }

    private fun getMSGQueueListApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param = MessageQueueParam(
                MySharedPreferences.getMySharedPreferences()!!.adminId!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
            )

            val call: Call<MSGQueueRes?>? =
                RetrofitRestClient.getInstance()?.msgQueueListApi(param)

            call?.enqueue(object : Callback<MSGQueueRes?> {
                override fun onResponse(
                    call: Call<MSGQueueRes?>,
                    response: Response<MSGQueueRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val userListResponse: MSGQueueRes = response.body()!!
                        msgQueueList = userListResponse.Data

                        msgQueueAdapter = MSGQueueAdapter(
                            requireActivity(),
                            msgQueueList,
                            this@MessageQueueFragment
                        )
                        recycleMSGQueue.adapter = msgQueueAdapter
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<MSGQueueRes?>, t: Throwable) {
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
            AppUtils.showToast(requireActivity(), getString(R.string.no_internet))
        }
    }

    fun acceptApi(CampaignGUID: String, ReplyFrom: String, position: Int) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val param =
                MsgAcceptParam(
                    MySharedPreferences.getMySharedPreferences()!!.adminId!!.toInt(),
                    CampaignGUID,
                    ReplyFrom,
                    MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                    MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                    MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
                )

            val call: Call<MsgAcceptRes?>? =
                RetrofitRestClient.getInstance()?.msgAcceptApi(param)

            call?.enqueue(object : Callback<MsgAcceptRes?> {
                override fun onResponse(
                    call: Call<MsgAcceptRes?>,
                    response: Response<MsgAcceptRes?>
                ) {
                    hideProgressDialog()
                    val userDeleteResponse: MsgAcceptRes = response.body()!!
                    if (response.isSuccessful) {
                        if (userDeleteResponse.Status) {
                            AppUtils.showToast(requireActivity(), userDeleteResponse.Message)
                            msgQueueList.removeAt(position)
                            msgQueueAdapter.notifyItemRemoved(position)
                            msgQueueAdapter.notifyItemRangeChanged(position, msgQueueList.size)
                        } else {
                            AppUtils.showToast(requireActivity(), userDeleteResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<MsgAcceptRes?>, t: Throwable) {
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
            AppUtils.showToast(requireActivity(), getString(R.string.no_internet))
        }
    }
}