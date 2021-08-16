package com.app.text2them.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.text2them.R
import com.app.text2them.adapter.ChatAdapterOneWay
import com.app.text2them.adapter.ChatAdapterTwoWay
import com.app.text2them.adapter.ContactSpinAdapter
import com.app.text2them.models.ChatModel.Chat
import com.app.text2them.models.ChatModel.ChatParam
import com.app.text2them.models.ChatModel.ChatResFull
import com.app.text2them.models.ChatModelSingle.ChatParamOne
import com.app.text2them.models.ChatModelSingle.ChatResOne
import com.app.text2them.models.ChatModelSingle.Sendmessage
import com.app.text2them.models.ChatSendMsgModel.SendMSGParam
import com.app.text2them.models.ChatSendMsgModel.SendMSGRes
import com.app.text2them.models.ContactModel.ContactParam
import com.app.text2them.models.ContactModel.ContactResponse
import com.app.text2them.models.ContactModel.Data
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ChatFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    var userGUID: String = ""
    var campaignId: Int = 0
    var communicationGUID: String = ""
    var replyFrom: String = ""
    var campaignGUID: String = ""

    private lateinit var chatAdapterTwoWay: ChatAdapterTwoWay
    private lateinit var chatAdapterOneWay: ChatAdapterOneWay
    var chatListTwoWay = ArrayList<Chat>()
    var chatListOneWay = ArrayList<Sendmessage>()


    var userList: List<Data>? = null

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
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            recycleTwoWay.layoutManager = LinearLayoutManager(requireActivity())
            recycleSingle.layoutManager = LinearLayoutManager(requireActivity())

            getUserList()

            spinContact.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        linearMessage.visibility = View.VISIBLE
                        val postData: Data = userList!![position - 1]
                        userGUID = postData.CommunicationGUID
                        chatListTwoWayApi(userGUID)
                    } else {
                        linearMessage.visibility = View.GONE
                        recycleSingle.visibility = View.GONE
                        recycleTwoWay.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            ivSend.setOnClickListener {
                if (!TextUtils.isEmpty(AppUtils.getText(edtMessage))) {
                    sendReplyApi(AppUtils.getText(edtMessage))
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Please enter message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getUserList() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param = ContactParam(
                MySharedPreferences.getMySharedPreferences()?.accessToken!!,
                MySharedPreferences.getMySharedPreferences()?.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()?.userId!!.toInt()
//                "62e1f167-3a64-4e9f-b665-04feec0fd29f",
//                2,
//                70
            )
            println(param)
            val call: Call<ContactResponse?>? =
                RetrofitRestClient.getInstance()?.contactList(param)

            call?.enqueue(object : Callback<ContactResponse?> {
                override fun onResponse(
                    call: Call<ContactResponse?>,
                    response: Response<ContactResponse?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val numberListResponse: ContactResponse = response.body()!!
                        if (numberListResponse.Status) {
                            userList = numberListResponse.Data
                            spinContact.adapter = ContactSpinAdapter(activity, userList)
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ContactResponse?>, t: Throwable) {
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

    fun chatListTwoWayApi(guID: String) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param =
                ChatParam(
                    guID,
                    MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                    MySharedPreferences.getMySharedPreferences()!!.userId!!
                )
            /*val param =
                ChatParam(
                    "d13221bb-be5b-416a-a3c3-6e4865cc72a4@txtmsgnow.com",
                    "62e1f167-3a64-4e9f-b665-04feec0fd29f",
                    "70"
                )*/

            val call: Call<ChatResFull?>? =
                RetrofitRestClient.getInstance()?.chatListTwoWay(param)

            call?.enqueue(object : Callback<ChatResFull?> {
                override fun onResponse(
                    call: Call<ChatResFull?>,
                    response: Response<ChatResFull?>
                ) {
                    if (response.isSuccessful) {
                        val chatResFull: ChatResFull = response.body()!!
                        if (chatResFull.Data.chatList != null && chatResFull.Data.chatList.size > 0) {
                            recycleTwoWay.visibility = View.VISIBLE
                            recycleSingle.visibility = View.GONE
                            hideProgressDialog()
                            campaignId = chatResFull.Data.CampaignId
                            communicationGUID = chatResFull.Data.CommunicationGUID
                            replyFrom = chatResFull.Data.ReplyFrom
                            campaignGUID = chatResFull.Data.CampaignGUID

                            chatListTwoWay = chatResFull.Data.chatList

                            chatAdapterTwoWay =
                                ChatAdapterTwoWay(chatListTwoWay, chatResFull.Data.ReplyFrom)
                            recycleTwoWay.smoothScrollToPosition(chatListTwoWay.size - 1)
                            recycleTwoWay.adapter = chatAdapterTwoWay
                        } else {
                            recycleTwoWay.visibility = View.GONE
                            recycleSingle.visibility = View.VISIBLE
                            chatListOneWayApi(userGUID)
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ChatResFull?>, t: Throwable) {
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

    fun chatListOneWayApi(guID: String) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {

            /*val param =
                ChatParamOne(
                    0,
                    "700c8909-594b-4116-bb70-90b791d5c382@txtmsgnow.com",
                    "62e1f167-3a64-4e9f-b665-04feec0fd29f",
                    MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                    70
                )*/
            val param =
                ChatParamOne(
                    MySharedPreferences.getMySharedPreferences()!!.adminId!!.toInt(),
                    guID,
                    MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                    MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                    MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
                )

            val call: Call<ChatResOne?>? =
                RetrofitRestClient.getInstance()?.chatListOneWay(param)

            call?.enqueue(object : Callback<ChatResOne?> {
                override fun onResponse(
                    call: Call<ChatResOne?>,
                    response: Response<ChatResOne?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val chatResFull: ChatResOne = response.body()!!
                        if (chatResFull.Data.sendmessageList != null && chatResFull.Data.sendmessageList.size > 0) {

                            campaignId = chatResFull.Data.sendmessageList[0].MarketingCampaignId
                            communicationGUID =
                                chatResFull.Data.sendmessageList[0].MarketingCampaignGUID
                            replyFrom = chatResFull.Data.sendmessageList[0].SenderNetworkid
                            campaignGUID = ""

                            chatListOneWay = chatResFull.Data.sendmessageList

                            chatAdapterOneWay = ChatAdapterOneWay(chatListOneWay)
                            recycleSingle.smoothScrollToPosition(chatListOneWay.size - 1)
                            recycleSingle.adapter = chatAdapterOneWay
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ChatResOne?>, t: Throwable) {
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

    private fun sendReplyApi(msg: String) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param =
                SendMSGParam(
                    campaignGUID,
                    campaignId,
                    communicationGUID,
                    msg,
                    replyFrom,
                    MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
                )

            val call: Call<SendMSGRes?>? =
                RetrofitRestClient.getInstance()?.sendMsgApi(param)

            call?.enqueue(object : Callback<SendMSGRes?> {
                override fun onResponse(
                    call: Call<SendMSGRes?>,
                    response: Response<SendMSGRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val ticketDetailsRes: SendMSGRes = response.body()!!
                        Toast.makeText(
                            requireActivity(),
                            ticketDetailsRes.Message,
                            Toast.LENGTH_SHORT
                        ).show()
                        edtMessage.setText("")
                        /*if (campaignGUID == "") {
                            chatListOneWayApi(userGUID)
                        } else {
                            chatListTwoWayApi(userGUID)
                        }*/
                        if (campaignGUID == "") {
                            chatListOneWayApi(userGUID)
                        } else {
                            chatListTwoWayApi(userGUID)
                        }

                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SendMSGRes?>, t: Throwable) {
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