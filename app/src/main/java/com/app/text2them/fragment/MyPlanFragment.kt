package com.app.text2them.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.text2them.R
import com.app.text2them.adapter.MyPlanAdapter
import com.app.text2them.models.MyPlanModel.MyPlanParam
import com.app.text2them.models.MyPlanModel.MyPlanResponse
import com.app.text2them.models.MyPlanModel.Usersubscription
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_my_plan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyPlanFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var myPlanAdapter: MyPlanAdapter
    var planList = ArrayList<Usersubscription>()

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
        return inflater.inflate(R.layout.fragment_my_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycleMyPlay.layoutManager = LinearLayoutManager(requireActivity())

        getMyPlanApi()
    }

    private fun getMyPlanApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

            val param = MyPlanParam(
                MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                        MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
            )

            val call: Call<MyPlanResponse?>? =
                RetrofitRestClient.getInstance()?.myPlanApi(param)

            call?.enqueue(object : Callback<MyPlanResponse?> {
                override fun onResponse(
                    call: Call<MyPlanResponse?>,
                    response: Response<MyPlanResponse?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val userListResponse: MyPlanResponse = response.body()!!
                        planList = userListResponse.Data.usersubscriptionList

                        myPlanAdapter = MyPlanAdapter(
                            requireActivity(),
                            planList
                        )
                        recycleMyPlay.adapter = myPlanAdapter
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }

                }

                override fun onFailure(call: Call<MyPlanResponse?>, t: Throwable) {
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPlanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}