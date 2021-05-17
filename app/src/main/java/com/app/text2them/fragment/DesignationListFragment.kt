package com.app.text2them.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.text2them.R
import com.app.text2them.adapter.DesignationAdapter
import com.app.text2them.models.DeleteModel.Desi_Depart_Delete_Param
import com.app.text2them.models.DepartmentModel.DepartmentListParam
import com.app.text2them.models.DesignationModel.Designation
import com.app.text2them.models.DesignationModel.DesignationListRes
import com.app.text2them.models.UserDeleteModel.UserDeleteResponse
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_department_list.*
import kotlinx.android.synthetic.main.fragment_designation_list.*
import kotlinx.android.synthetic.main.fragment_user_add.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DesignationListFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var desginationList: ArrayList<Designation>? = null
    private lateinit var designationAdapter: DesignationAdapter

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
        return inflater.inflate(R.layout.fragment_designation_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isAdded) {
            getDesignationApi()
        }
    }

    private fun getDesignationApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val param = DepartmentListParam(
                MySharedPreferences.getMySharedPreferences()?.accessToken!!,
                MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
            )

            val call: Call<DesignationListRes?>? =
                RetrofitRestClient.getInstance()?.getDesignationListApi(param)

            call?.enqueue(object : Callback<DesignationListRes?> {
                override fun onResponse(
                    call: Call<DesignationListRes?>,
                    response: Response<DesignationListRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val numberListResponse: DesignationListRes = response.body()!!
                        if (numberListResponse.Status) {
                            desginationList = numberListResponse.Data.designationList
                            recycleDesignation.layoutManager =
                                LinearLayoutManager(requireActivity())
                            designationAdapter = DesignationAdapter(
                                requireActivity(),
                                desginationList!!,
                                this@DesignationListFragment
                            )
                            recycleDesignation.adapter = designationAdapter
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onFailure(call: Call<DesignationListRes?>, t: Throwable) {
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

    fun deleteConfirmDialog(id: Int, position: Int) {
        AlertDialog.Builder(requireActivity())
            .setMessage("Are you sure to delete this Designation?")
            .setPositiveButton(getString(R.string.yes)) { dialogInterface, i ->
                dialogInterface.dismiss()
                deleteDepartmentApi(id, position)
            }
            .setNegativeButton(getString(R.string.no)) { dialogInterface, i -> dialogInterface.dismiss() }
            .show()
    }

    private fun deleteDepartmentApi(id: Int, position: Int) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val userDeleteParam =
                Desi_Depart_Delete_Param(
                    MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                    MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(), id
                )

            val call: Call<UserDeleteResponse?>? =
                RetrofitRestClient.getInstance()?.departmentDeleteApi(userDeleteParam)

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
                            desginationList!!.removeAt(position)
                            designationAdapter.notifyItemRemoved(position)
                            designationAdapter.notifyItemRangeChanged(
                                position,
                                desginationList!!.size
                            )
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
}