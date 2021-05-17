package com.app.text2them.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.text2them.R
import com.app.text2them.adapter.DepartmentAdapter
import com.app.text2them.adapter.DepartmentSpinnerAdapter
import com.app.text2them.adapter.UserListAdapter
import com.app.text2them.models.DepartmentModel.Department
import com.app.text2them.models.DepartmentModel.DepartmentListParam
import com.app.text2them.models.DepartmentModel.DepartmentListRes
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_department_list.*
import kotlinx.android.synthetic.main.fragment_user_add.*
import kotlinx.android.synthetic.main.fragment_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DepartmentListFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var departmentList: List<Department>? = null
    private lateinit var  departmentAdapter: DepartmentAdapter

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
        return inflater.inflate(R.layout.fragment_department_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded){
            getDepartmentApi()
        }
    }

    private fun getDepartmentApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val param = DepartmentListParam(
                MySharedPreferences.getMySharedPreferences()?.accessToken!!,
                MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
            )

            val call: Call<DepartmentListRes?>? =
                RetrofitRestClient.getInstance()?.getDepartmentListApi(param)

            call?.enqueue(object : Callback<DepartmentListRes?> {
                override fun onResponse(
                    call: Call<DepartmentListRes?>,
                    response: Response<DepartmentListRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val numberListResponse: DepartmentListRes = response.body()!!
                        if (numberListResponse.Status) {
                            departmentList = numberListResponse.Data.departmentList

                            recycleDepartment.layoutManager = LinearLayoutManager(requireActivity())
                            departmentAdapter = DepartmentAdapter(
                                requireActivity(),
                                departmentList!!,
                                this@DepartmentListFragment
                            )
                            recycleDepartment.adapter = departmentAdapter
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DepartmentListRes?>, t: Throwable) {
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