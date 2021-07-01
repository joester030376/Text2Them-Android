package com.app.text2them.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.text2them.R
import com.app.text2them.adapter.UserListAdapter
import com.app.text2them.dialog.ChangeNumberDialog
import com.app.text2them.dialog.ViewUserDetailsDialog
import com.app.text2them.models.ChangeNumberModel.ChangeNumberParam
import com.app.text2them.models.ChangeNumberModel.ChangeNumberRes
import com.app.text2them.models.UserDeleteModel.UserDeleteParam
import com.app.text2them.models.UserDeleteModel.UserDeleteResponse
import com.app.text2them.models.UserDetailModel.UserDetailsParam
import com.app.text2them.models.UserDetailModel.UserDetailsResponse
import com.app.text2them.models.UserListModel.Staffmember
import com.app.text2them.models.UserListModel.UserListParam
import com.app.text2them.models.UserListModel.UserListResponse
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.dialog_change_number.*
import kotlinx.android.synthetic.main.dialog_change_number.btnSubmit
import kotlinx.android.synthetic.main.dialog_change_number.edtNumber
import kotlinx.android.synthetic.main.dialog_user_details.*
import kotlinx.android.synthetic.main.fragment_user_add.*
import kotlinx.android.synthetic.main.fragment_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UsersFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var userListAdapter: UserListAdapter
    var staffList = ArrayList<Staffmember>()

    var userDetailsDialog: ViewUserDetailsDialog? = null
    var changeNumberDialog: ChangeNumberDialog? = null

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
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            getUserListApi()
        }

        btnAddNew.setOnClickListener {
            val b = Bundle()
            b.putString("userID", "ID")

            val fragment = UserAddFragment()
            fragment.arguments = b
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
        }
    }

    private fun getUserListApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param = UserListParam(
                MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
            )

            val call: Call<UserListResponse?>? =
                RetrofitRestClient.getInstance()?.userListApi(param)

            call?.enqueue(object : Callback<UserListResponse?> {
                override fun onResponse(
                    call: Call<UserListResponse?>,
                    response: Response<UserListResponse?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val userListResponse: UserListResponse = response.body()!!
                        staffList = userListResponse.Data.staffmemberList

                        linearLayoutManager = LinearLayoutManager(requireActivity())
                        recycleUser.layoutManager = linearLayoutManager
                        userListAdapter = UserListAdapter(
                            requireActivity(),
                            staffList,
                            this@UsersFragment
                        )
                        recycleUser.adapter = userListAdapter
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }

                }

                override fun onFailure(call: Call<UserListResponse?>, t: Throwable) {
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

    fun getUserDetailsApi(id: Int) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val userDetailsParam = UserDetailsParam(id)

            val call: Call<UserDetailsResponse?>? =
                RetrofitRestClient.getInstance()?.userDetailsApi(userDetailsParam)

            call?.enqueue(object : Callback<UserDetailsResponse?> {
                override fun onResponse(
                    call: Call<UserDetailsResponse?>,
                    response: Response<UserDetailsResponse?>
                ) {
                    hideProgressDialog()
                    val userDetailsResponse: UserDetailsResponse = response.body()!!
                    if (response.isSuccessful) {
                        if (userDetailsResponse.Status) {
                            userDetailsDialog = ViewUserDetailsDialog(requireActivity())
                            userDetailsDialog?.show()

                            userDetailsDialog?.txtFName!!.text = userDetailsResponse.Data.FirstName
                            userDetailsDialog?.txtLName!!.text = userDetailsResponse.Data.LAstname
                            userDetailsDialog?.txtEmail!!.text = userDetailsResponse.Data.Email
                            userDetailsDialog?.txtDepartment!!.text =
                                userDetailsResponse.Data.Department
                            userDetailsDialog?.txtDesignation!!.text =
                                userDetailsResponse.Data.Designation
                            userDetailsDialog?.txtMobileNumber!!.text =
                                userDetailsResponse.Data.Mobilenumber
                            userDetailsDialog?.txtWorkingTime!!.text =
                                userDetailsResponse.Data.WorkTimings
                            userDetailsDialog?.txtIP!!.text = userDetailsResponse.Data.ip
                            userDetailsDialog?.txtCountry!!.text = userDetailsResponse.Data.Country
                            userDetailsDialog?.txtState!!.text = userDetailsResponse.Data.State
                            userDetailsDialog?.txtCity!!.text = userDetailsResponse.Data.CityName
                            userDetailsDialog?.txtZipCode!!.text = userDetailsResponse.Data.ZipCode

                        } else {
                            AppUtils.showToast(requireActivity(), userDetailsResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<UserDetailsResponse?>, t: Throwable) {
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

    fun deleteConfirmDialog(id: Int, position: Int) {
        AlertDialog.Builder(requireActivity())
            .setMessage("Are you sure to delete the user?")
            .setPositiveButton(getString(R.string.yes)) { dialogInterface, i ->
                dialogInterface.dismiss()
                deleteUserApi(id, position)
            }
            .setNegativeButton(getString(R.string.no)) { dialogInterface, i -> dialogInterface.dismiss() }
            .show()
    }

    fun editUser(id: Int) {
        val b = Bundle()
        b.putString("userID", id.toString())

        val fragment = UserAddFragment()
        fragment.arguments = b
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    private fun deleteUserApi(id: Int, position: Int) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val userDeleteParam =
                UserDeleteParam(MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(), id)

            val call: Call<UserDeleteResponse?>? =
                RetrofitRestClient.getInstance()?.userDeleteApi(userDeleteParam)

            call?.enqueue(object : Callback<UserDeleteResponse?> {
                override fun onResponse(
                    call: Call<UserDeleteResponse?>,
                    response: Response<UserDeleteResponse?>
                ) {
                    hideProgressDialog()
                    val userDeleteResponse: UserDeleteResponse = response.body()!!
                    if (response.isSuccessful) {
                        if (userDeleteResponse.Status) {
                            AppUtils.showToast(requireActivity(), userDeleteResponse.Message)
                            staffList.removeAt(position)
                            userListAdapter.notifyItemRemoved(position)
                            userListAdapter.notifyItemRangeChanged(position, staffList.size)
                        } else {
                            AppUtils.showToast(requireActivity(), userDeleteResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<UserDeleteResponse?>, t: Throwable) {
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

    fun changeNumberDialog(id: Int) {
        changeNumberDialog = ChangeNumberDialog(requireActivity())
        changeNumberDialog!!.show()

        changeNumberDialog!!.btnSubmit.setOnClickListener {
            when {
                AppUtils.isEmpty(changeNumberDialog!!.edtNumber.text.toString()) -> {
                    //AppUtils.showToast(requireActivity(), "Please enter mobile number")
                    changeNumberDialog!!.edtNumber.error = "Please enter mobile number"
                }
                AppUtils.getText(changeNumberDialog!!.edtNumber).length < 10 -> {
                    //AppUtils.showToast(requireActivity(), "Please enter valid mobile number")
                    changeNumberDialog!!.edtNumber.error = "Please enter valid mobile number"
                }
                else -> {
                    changeNumberApi(id, AppUtils.getText(changeNumberDialog!!.edtNumber))
                }
            }
        }
    }

    private fun changeNumberApi(id: Int, number: String) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val param =
                ChangeNumberParam(
                    number,
                    id,
                    MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                    MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
                )

            val call: Call<ChangeNumberRes?>? =
                RetrofitRestClient.getInstance()?.changeNumberApi(param)

            call?.enqueue(object : Callback<ChangeNumberRes?> {
                override fun onResponse(
                    call: Call<ChangeNumberRes?>,
                    response: Response<ChangeNumberRes?>
                ) {
                    hideProgressDialog()
                    val changeNumberRes: ChangeNumberRes = response.body()!!
                    if (response.isSuccessful) {
                        if (changeNumberRes.Status) {
                            AppUtils.showToast(requireActivity(), changeNumberRes.Message)
                            changeNumberDialog!!.dismiss()
                            getUserListApi()
                        } else {
                            AppUtils.showToast(requireActivity(), changeNumberRes.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<ChangeNumberRes?>, t: Throwable) {
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