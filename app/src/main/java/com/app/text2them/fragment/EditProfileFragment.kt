package com.app.text2them.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.app.text2them.R
import com.app.text2them.adapter.CountryAdapter
import com.app.text2them.adapter.StateAdapter
import com.app.text2them.models.CountryModel.Country
import com.app.text2them.models.CountryModel.CountryListResponse
import com.app.text2them.models.CountryModel.CountryParam
import com.app.text2them.models.EditProfileModel.OrganizationParam
import com.app.text2them.models.EditProfileModel.ProfileDetailsParam
import com.app.text2them.models.EditProfileModel.ProfileResponse
import com.app.text2them.models.GetProfile.Data
import com.app.text2them.models.StateModel.State
import com.app.text2them.models.StateModel.StateResponse
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditProfileFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var countryList: List<Country>? = null
    private var stateList: List<State>? = null
    private var countryId: String = ""
    private var stateId: String = ""
    private var stateName: String = ""
    private var countryName: String = ""

    lateinit var data: Data

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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = this.arguments
        if (b != null) {
            data = b.getSerializable("ProfileData") as Data

            edtFName.setText(data.Name)
            edtLname.setText(data.LastName)
            edtEmail.setText(data.Email)
            edtPassword.setText(data.Password)
            edtOrgName.setText(data.OrganizationName)
            edtDesc.setText(data.Description)
            edtWebsite.setText(data.website)
            edtDataPurge.setText(data.Datapurge)
            edtCity.setText(data.city)
            edtZipCode.setText(data.ZipCode)
            edtFName.setText(data.Name)
            countryName = data.CountryName
            stateName = data.StateName
        }

        btnCancel.setOnClickListener {
            val fragment = ProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()
        }
        btnSave.setOnClickListener {
            if (validation()) {
                editProfileApi()
            }
        }

        getCountryApi()

        spinCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val postData: Country = countryList!![position - 1]
                    countryId = postData.id.toString()
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
                    stateId = postData.id.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun validation(): Boolean {
        when {
            TextUtils.isEmpty(edtFName.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter first name", Toast.LENGTH_SHORT).show()
                return false
            }
            TextUtils.isEmpty(edtLname.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter last name", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(edtEmail.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter email", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(edtPassword.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter password", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(edtOrgName.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter organization name", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(edtDesc.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter description", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(edtWebsite.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter website", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(edtDataPurge.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter data purge", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(countryId) -> {
                Toast.makeText(requireContext(), "Please select country", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(stateId) -> {
                Toast.makeText(requireContext(), "Please select state", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(edtCity.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter city", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(edtZipCode.text.toString()) -> {
                Toast.makeText(requireContext(), "Please enter zip code", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun getCountryApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())

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
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val numberListResponse: CountryListResponse = response.body()!!
                        if (numberListResponse.Status) {
                            countryList = numberListResponse.Data.countryList
                            spinCountry.adapter =
                                CountryAdapter(activity, countryList, false)
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

    private fun editProfileApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val profileParam = ProfileDetailsParam(
                AppUtils.getText(edtEmail),
                AppUtils.getText(edtLname),
                AppUtils.getText(edtFName),
                AppUtils.getText(edtPassword),
                MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                0,
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
            )

            val call: Call<ProfileResponse?>? =
                RetrofitRestClient.getInstance()?.editProfileDetailsApi(profileParam)

            call?.enqueue(object : Callback<ProfileResponse?> {
                override fun onResponse(
                    call: Call<ProfileResponse?>,
                    response: Response<ProfileResponse?>
                ) {
                    val getProfileResponse: ProfileResponse = response.body()!!
                    if (response.isSuccessful) {
                        if (getProfileResponse.Status) {
                            //AppUtils.showToast(requireActivity(), getProfileResponse.Message)
                            editOrganizationApi(getProfileResponse.Message)
                        } else {
                            AppUtils.showToast(requireActivity(), getProfileResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {
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

    private fun editOrganizationApi(message: String) {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            val param = OrganizationParam(
                countryId.toInt(),
                AppUtils.getText(edtDataPurge),
                AppUtils.getText(edtDesc),
                data.OrganizationId,
                AppUtils.getText(edtOrgName),
                MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                0,
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
                AppUtils.getText(edtZipCode),
                AppUtils.getText(edtCity),
                stateId.toInt(),
                AppUtils.getText(edtWebsite),
            )

            val call: Call<ProfileResponse?>? =
                RetrofitRestClient.getInstance()?.editOrgDetailsApi(param)

            call?.enqueue(object : Callback<ProfileResponse?> {
                override fun onResponse(
                    call: Call<ProfileResponse?>,
                    response: Response<ProfileResponse?>
                ) {
                    hideProgressDialog()
                    val getProfileResponse: ProfileResponse = response.body()!!
                    if (response.isSuccessful) {
                        if (getProfileResponse.Status) {
                            AppUtils.showToast(requireActivity(), message)
                            val fragment = ProfileFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                                .commit()
                        } else {
                            AppUtils.showToast(requireActivity(), getProfileResponse.Message)
                        }
                    } else {
                        AppUtils.showToast(requireActivity(), response.message())
                    }
                }

                override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {
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