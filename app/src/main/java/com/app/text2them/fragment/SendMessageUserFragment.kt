package com.app.text2them.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.text2them.R
import com.app.text2them.adapter.*
import com.app.text2them.models.DropModel.*
import com.app.text2them.models.MessageToUserModel.*
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_send_message_user.*
import kotlinx.android.synthetic.main.fragment_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SendMessageUserFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var userTypeList: ArrayList<String>? = ArrayList()
    var userTypeS: String = ""
    var userType: Int = 0

    private var contactList: List<Contactgrp>? = null
    private var departmentList: List<Department>? = null
    private var designationList: List<Designation>? = null

    var contactID: Int = 0
    var departmentID: Int = 0
    var designationID: Int = 0

    private lateinit var sendMessageToUserAdapter: SendMessageToUserAdapter
    var userList = ArrayList<UserListData>()
    var selectedUserList = ArrayList<SelectedUser>()

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
        return inflater.inflate(R.layout.fragment_send_message_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleUserList.layoutManager = LinearLayoutManager(requireActivity())

        userTypeList?.add("Staff Member")
        userTypeList?.add("Contacts")

        spinUserType.adapter = UserTypeSpinAdapter(
            activity,
            userTypeList
        )

        spinUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    userTypeS = userTypeList!![position - 1]
                    btnSearch.visibility = View.VISIBLE
                    recycleUserList.visibility = View.VISIBLE
                    if (userTypeS == "Staff Member") {
                        userType = 1
                        layoutContact.visibility = View.GONE
                        layoutDepartment.visibility = View.VISIBLE
                        layoutDesignation.visibility = View.VISIBLE
                    } else {
                        userType = 2
                        layoutContact.visibility = View.VISIBLE
                        layoutDepartment.visibility = View.GONE
                        layoutDesignation.visibility = View.GONE
                    }
                } else {
                    recycleUserList.visibility = View.INVISIBLE
                    btnSearch.visibility = View.GONE
                    userType = 0
                    layoutContact.visibility = View.GONE
                    layoutDepartment.visibility = View.GONE
                    layoutDesignation.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinContact.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    contactID = contactList!![position - 1].id
                } else {
                    contactID = 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spinDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    departmentID = departmentList!![position - 1].id
                } else {
                    departmentID = 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinDesignation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    designationID = designationList!![position - 1].id
                } else {
                    designationID = 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        getDropDownDataApi()

        btnSearch.setOnClickListener {
            if (userType == 1) {
                if (departmentID != 0 && designationID != 0) {
                    getUserListApi()
                } else {
                    AppUtils.showToast(
                        requireActivity(),
                        "Please Select Department and Designation"
                    )
                }
            } else if (userType == 2) {
                if (contactID != 0) {
                    getUserListApi()
                } else {
                    AppUtils.showToast(requireActivity(), "Please Select Contact Group")
                }
            }
        }

        btnSend.setOnClickListener {
            when {
                TextUtils.isEmpty(AppUtils.getText(edtSubject)) -> {
                    AppUtils.showToast(requireActivity(), "Please enter Subject")
                }
                TextUtils.isEmpty(AppUtils.getText(edtMessage)) -> {
                    AppUtils.showToast(requireActivity(), "Please enter Message")
                }
                selectedUserList.size == 0 -> {
                    AppUtils.showToast(requireActivity(), "Please select users")
                }
                else -> {
                    sendMessageApi()
                }
            }
        }
    }

    private fun getDropDownDataApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param = DropDownParam(
                MySharedPreferences.getMySharedPreferences()!!.adminId!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                MySharedPreferences.getMySharedPreferences()?.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
                userType
            )

            val call: Call<DropDownRes?>? =
                RetrofitRestClient.getInstance()?.dropDownApi(param)

            call?.enqueue(object : Callback<DropDownRes?> {
                override fun onResponse(
                    call: Call<DropDownRes?>,
                    response: Response<DropDownRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val dropDownRes: DropDownRes = response.body()!!
                        if (dropDownRes.Status) {
                            contactList = dropDownRes.Data.contactgrpList
                            departmentList = dropDownRes.Data.DepartmentList
                            designationList = dropDownRes.Data.DesignationList
                            spinContact.adapter = DropDownContactAdapter(activity, contactList)
                            spinDepartment.adapter =
                                DropDownDepartmentAdapter(activity, departmentList)
                            spinDesignation.adapter =
                                DropDownDesignationAdapter(activity, designationList)
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DropDownRes?>, t: Throwable) {
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

    private fun getUserListApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param = MessageToUserParam(
                MySharedPreferences.getMySharedPreferences()!!.adminId!!.toInt(),
                contactID,
                departmentID,
                designationID,
                MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                MySharedPreferences.getMySharedPreferences()?.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
                userType
            )

            val call: Call<MessageToUserListRes?>? =
                RetrofitRestClient.getInstance()?.messageToUserListApi(param)

            call?.enqueue(object : Callback<MessageToUserListRes?> {
                override fun onResponse(
                    call: Call<MessageToUserListRes?>,
                    response: Response<MessageToUserListRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val userListResponse: MessageToUserListRes = response.body()!!
                        userList = userListResponse.Data

                        sendMessageToUserAdapter = SendMessageToUserAdapter(
                            userType,
                            userList,
                            this@SendMessageUserFragment
                        )
                        recycleUserList.adapter = sendMessageToUserAdapter
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<MessageToUserListRes?>, t: Throwable) {
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

    fun addValue(networkEmail: String, phonenumber: String) {
        selectedUserList.add(SelectedUser(phonenumber, networkEmail))
        Log.e("@123Added", selectedUserList.toString())
    }

    fun removeValue(networkEmail: String, phonenumber: String) {
        selectedUserList.remove(SelectedUser(phonenumber, networkEmail))
        Log.e("@123Removed", selectedUserList.toString())
    }

    private fun sendMessageApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param = SendMessageToUserParam(
                MySharedPreferences.getMySharedPreferences()!!.adminId!!.toInt(),
                selectedUserList,
                AppUtils.getText(edtMessage),
                AppUtils.getText(edtSubject),
                MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                MySharedPreferences.getMySharedPreferences()?.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
            )
            println(param)
            val call: Call<SendMessageToUserRes?>? =
                RetrofitRestClient.getInstance()?.sendMessageToUserListApi(param)

            call?.enqueue(object : Callback<SendMessageToUserRes?> {
                override fun onResponse(
                    call: Call<SendMessageToUserRes?>,
                    response: Response<SendMessageToUserRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val sendMessageToUserRes: SendMessageToUserRes = response.body()!!
                        AppUtils.showToast(requireActivity(), sendMessageToUserRes.Message)
                        edtSubject.setText("")
                        edtMessage.setText("")
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<SendMessageToUserRes?>, t: Throwable) {
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