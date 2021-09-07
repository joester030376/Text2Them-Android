package com.app.text2them.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import com.app.text2them.models.UserAddModel.UserAddParam
import com.app.text2them.models.UserAddModel.UserAddResponse
import com.app.text2them.models.UserDetailModel.UserDetailsParam
import com.app.text2them.models.UserDetailModel.UserDetailsResponse
import com.app.text2them.models.UserEditModel.EditUserParam
import com.app.text2them.models.UserEditModel.EditUserResponse
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
    private var countryId: Int = 0
    private var stateId: Int = 0
    private var departmentId: Int = 0
    private var designationId: Int = 0
    private var stateName: String = ""
    private var countryName: String = ""
    private var userPassword: String = ""
    private var staffId: Int = 0

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
            if (userId == "ID") {
                getDepartmentApi("")
                edtNumber.isFocusable = true
                edtNumber.isFocusable = true
                edtNumber.isClickable = true
                edtNumber.isCursorVisible = true
            } else {
                getUserDetailsApi(userId.toInt())
                edtNumber.isFocusable = false
                edtNumber.isFocusable = false
                edtNumber.isClickable = false
                edtNumber.isCursorVisible = false
            }

        }

        btnSubmit.setOnClickListener {
            if (validation()) {
                if (userId == "ID") {
                    addNewUser()
                } else {
                    editUserDetail(userId.toInt())
                }
            }
        }

        spinCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val postData: Country = countryList!![position - 1]
                    countryId = postData.id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        spinState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val postData: State = stateList!![position - 1]
                    stateId = postData.id
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
                    val postData: Department = departmentList!![position - 1]
                    departmentId = postData.id
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
                    val postData: Designation = desginationList!![position - 1]
                    designationId = postData.id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        radioActive.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioActive.isChecked = true
                radioInActive.isChecked = false
            }
        }

        radioInActive.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                radioInActive.isChecked = true
                radioActive.isChecked = false
            }
        }
    }

    private fun getUserDetailsApi(id: Int) {
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
                            userPassword = userDetailsResponse.Data.Password


                            getDepartmentApi(userDetailsResponse.Data.Department)

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
            getDesignationApi()
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
            getCountryApi()
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
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

    private fun validation(): Boolean {
        when {
            AppUtils.isEmpty(edtFName.text.toString()) -> {
                //AppUtils.showToast(requireActivity(), "Please enter first name")
                edtFName.error = "Please enter first name"
                return false
            }
            AppUtils.isEmpty(edtLName.text.toString()) -> {
                //AppUtils.showToast(requireActivity(), "Please enter last name")
                edtLName.error = "Please enter last name"
                return false
            }
            departmentId == 0 -> {
                AppUtils.showToast(requireActivity(), "Please select department")
                return false
            }
            designationId == 0 -> {
                AppUtils.showToast(requireActivity(), "Please select designation")
                return false
            }
            AppUtils.isEmpty(edtNumber.text.toString()) -> {
                //AppUtils.showToast(requireActivity(), "Please enter mobile number")
                edtNumber.error = "Please enter mobile number"
                return false
            }
            AppUtils.getText(edtNumber).length < 10 -> {
                //AppUtils.showToast(requireActivity(), "Please enter valid mobile number")
                edtNumber.error = "Please enter valid mobile number"
                return false
            }
            AppUtils.isEmpty(edtEMail.text.toString()) -> {
                //AppUtils.showToast(requireActivity(), "Please enter email")
                edtEMail.error = "Please enter email"
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEMail.text.toString()).matches() -> {
                //AppUtils.showToast(requireActivity(), "Please enter valid email")
                edtEMail.error = "Please enter valid email"
                return false
            }
            AppUtils.isEmpty(edtWorkTime.text.toString()) -> {
                //AppUtils.showToast(requireActivity(), "Please enter working time")
                edtWorkTime.error = "Please enter working time"
                return false
            }
            countryId == 0 -> {
                AppUtils.showToast(requireActivity(), "Please select country")
                return false
            }
            stateId == 0 -> {
                AppUtils.showToast(requireActivity(), "Please select state")
                return false
            }
            AppUtils.isEmpty(edtCity.text.toString()) -> {
                //AppUtils.showToast(requireActivity(), "Please enter city")
                edtCity.error = "Please enter city"
                return false
            }
            AppUtils.isEmpty(edtZipCode.text.toString()) -> {
                //AppUtils.showToast(requireActivity(), "Please enter zip code")
                edtZipCode.error = "Please enter zip code"
                return false
            }
            AppUtils.getText(edtZipCode).length < 5 -> {
                edtZipCode.error = "Please enter valid zip code"
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun editUserDetail(staffId: Int) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val param = EditUserParam(
                edtCity.text.toString(),
                countryId,
                departmentId,
                designationId,
                edtEMail.text.toString(),
                edtFName.text.toString(),
                edtIP.text.toString(),
                true,
                edtLName.text.toString(),
                edtNumber.text.toString(),
                stateId,
                MySharedPreferences.getMySharedPreferences()?.accessToken!!,
                MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
                edtWorkTime.text.toString(),
                edtZipCode.text.toString(),
                staffId
            )

            val call: Call<EditUserResponse?>? =
                RetrofitRestClient.getInstance()?.editUserApi(param)

            call?.enqueue(object : Callback<EditUserResponse?> {
                override fun onResponse(
                    call: Call<EditUserResponse?>,
                    response: Response<EditUserResponse?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val editUserResponse: EditUserResponse = response.body()!!
                        if (editUserResponse.Status) {
                            AppUtils.showToast(requireActivity(), editUserResponse.Message)
                            val fragment = UsersFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                                .commit()
                        } else {
                            AppUtils.showToast(requireActivity(), editUserResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<EditUserResponse?>, t: Throwable) {
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

    private fun addNewUser() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val param = UserAddParam(
                edtCity.text.toString(),
                countryId,
                departmentId,
                designationId,
                edtEMail.text.toString(),
                edtFName.text.toString(),
                edtIP.text.toString(),
                true,
                edtLName.text.toString(),
                edtNumber.text.toString(),
                stateId,
                MySharedPreferences.getMySharedPreferences()?.accessToken!!,
                MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
                edtWorkTime.text.toString(),
                edtZipCode.text.toString()
            )

            val call: Call<UserAddResponse?>? =
                RetrofitRestClient.getInstance()?.saveUserApi(param)

            call?.enqueue(object : Callback<UserAddResponse?> {
                override fun onResponse(
                    call: Call<UserAddResponse?>,
                    response: Response<UserAddResponse?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val editUserResponse: UserAddResponse = response.body()!!
                        if (editUserResponse.Status) {
                            AppUtils.showToast(requireActivity(), editUserResponse.Message)
                            val fragment = UsersFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                                .commit()
                        } else {
                            AppUtils.showToast(requireActivity(), editUserResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<UserAddResponse?>, t: Throwable) {
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
}