package com.app.text2them.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.app.text2them.R
import com.app.text2them.adapter.CountryAdapter
import com.app.text2them.adapter.DepartmentSpinnerAdapter
import com.app.text2them.adapter.DesignationSpinnerAdapter
import com.app.text2them.adapter.StateAdapter
import com.app.text2them.models.CountryModel.Country
import com.app.text2them.models.CountryModel.CountryListResponse
import com.app.text2them.models.CountryModel.CountryParam
import com.app.text2them.models.DepartmentModel.Department
import com.app.text2them.models.DepartmentModel.DepartmentListParam
import com.app.text2them.models.DepartmentModel.DepartmentListRes
import com.app.text2them.models.DesignationModel.Designation
import com.app.text2them.models.DesignationModel.DesignationListRes
import com.app.text2them.models.StateModel.State
import com.app.text2them.models.StateModel.StateResponse
import com.app.text2them.models.UserDetailModel.UserDetailsParam
import com.app.text2them.models.UserDetailModel.UserDetailsResponse
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_user_add.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserAddFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var userId: String = ""

    private var countryList: List<Country>? = null
    private var stateList: List<State>? = null
    private var departmentList: List<Department>? = null
    private var desginationList: List<Designation>? = null
    private var countryId: String = ""
    private var stateId: String = ""
    private var stateName: String = ""
    private var countryName: String = ""

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
        return inflater.inflate(R.layout.fragment_user_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = this.arguments
        if (b != null) {
            userId = b.getString("userID") as String
            getUserDetailsApi(userId.toInt())
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

                            edtFName.setText(userDetailsResponse.Data.FirstName)
                            edtLName.setText(userDetailsResponse.Data.LAstname)
                            edtEMail.setText(userDetailsResponse.Data.Email)
                            //txtDepartment.setText(userDetailsResponse.Data.Department)
                            //txtDesignation.text = userDetailsResponse.Data.Designation
                            edtNumber.setText(userDetailsResponse.Data.Mobilenumber)
                            edtWorkTime.setText(userDetailsResponse.Data.WorkTimings)
                            edtIP.setText(userDetailsResponse.Data.ip)
                            //txtCountry.text = userDetailsResponse.Data.Country
                            //txtState.text = userDetailsResponse.Data.State
                            edtCity.setText(userDetailsResponse.Data.CityName)
                            edtZipCode.setText(userDetailsResponse.Data.ZipCode)

                            getCountryApi()
                            getStateApi()
                            getDepartmentApi(userDetailsResponse.Data.Department)
                            getDesignationApi()
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

    private fun getCountryApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            //showProgressDialog(requireActivity())

            val param = CountryParam(
                MySharedPreferences.getMySharedPreferences()?.accessToken!!
            )

            val call: Call<CountryListResponse?>? =
                RetrofitRestClient.getInstance()?.getCountryListApi(param)

            call?.enqueue(object : Callback<CountryListResponse?> {
                override fun onResponse(
                    call: Call<CountryListResponse?>,
                    response: Response<CountryListResponse?>
                ) {
                    //hideProgressDialog()
                    if (response.isSuccessful) {
                        val numberListResponse: CountryListResponse = response.body()!!
                        if (numberListResponse.Status) {
                            countryList = numberListResponse.Data.countryList
                            spinCountry.adapter =
                                CountryAdapter(activity, countryList, true)
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CountryListResponse?>, t: Throwable) {
                    //hideProgressDialog()
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
            getStateApi()
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getStateApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {

            val param = CountryParam(
                MySharedPreferences.getMySharedPreferences()?.accessToken!!
            )

            val call: Call<StateResponse?>? =
                RetrofitRestClient.getInstance()?.getStateListApi(param)

            call?.enqueue(object : Callback<StateResponse?> {
                override fun onResponse(
                    call: Call<StateResponse?>,
                    response: Response<StateResponse?>
                ) {
                    if (response.isSuccessful) {
                        val numberListResponse: StateResponse = response.body()!!
                        if (numberListResponse.Status) {
                            stateList = numberListResponse.Data.stateList
                            spinState.adapter =
                                StateAdapter(activity, stateList, true)
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<StateResponse?>, t: Throwable) {
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

    private fun getDepartmentApi(department: String) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {

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
                    if (response.isSuccessful) {
                        val numberListResponse: DepartmentListRes = response.body()!!
                        if (numberListResponse.Status) {
                            departmentList = numberListResponse.Data.departmentList
                            spinDepartment.adapter =
                                DepartmentSpinnerAdapter(activity, departmentList)

                            for (i in departmentList!!.indices) {
                                if (department == departmentList!![i].Name) {
                                    spinDepartment.setSelection(i)
                                    break
                                }
                            }

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

    private fun getDesignationApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {

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
                    if (response.isSuccessful) {
                        val numberListResponse: DesignationListRes = response.body()!!
                        if (numberListResponse.Status) {
                            desginationList = numberListResponse.Data.designationList
                            spinDesignation.adapter =
                                DesignationSpinnerAdapter(activity, desginationList)
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