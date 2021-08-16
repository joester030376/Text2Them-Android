package com.app.text2them.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.text2them.R
import com.app.text2them.models.GetProfile.Data
import com.app.text2them.models.GetProfile.GetProfileParam
import com.app.text2them.models.GetProfile.GetProfileResponse
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.bumptech.glide.Glide
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnEdit.setOnClickListener {

            val b = Bundle()
            b.putSerializable("ProfileData", data)

            val fragment = EditProfileFragment()
            fragment.arguments = b
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()

            /*val fragB = EditProfileFragment()
            fragB.arguments = b
            fragmentManager!!.beginTransaction().replace(R.id.container, fragB)*/
        }
        if (isAdded) {
            getProfileDataApi()
        }
    }

    private fun getProfileDataApi() {
        if (AppUtils.isConnectedToInternet(requireActivity())) {
            showProgressDialog(requireActivity())
            val getProfileParam = GetProfileParam(
                MySharedPreferences.getMySharedPreferences()!!.accessToken!!,
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt()
            )

            val call: Call<GetProfileResponse?>? =
                RetrofitRestClient.getInstance()?.getProfileDataApi(getProfileParam)

            call?.enqueue(object : Callback<GetProfileResponse?> {
                override fun onResponse(
                    call: Call<GetProfileResponse?>,
                    response: Response<GetProfileResponse?>
                ) {
                    hideProgressDialog()
                    val getProfileResponse: GetProfileResponse = response.body()!!
                    if (response.isSuccessful) {
                        if (getProfileResponse.Status) {
                            data = getProfileResponse.Data
                            txtFName.text = getProfileResponse.Data.Name
                            txtLname.text = getProfileResponse.Data.LastName
                            txtEmail.text = getProfileResponse.Data.Email
                            txtPassword.text = getProfileResponse.Data.Password
                            txtOrgName.text = getProfileResponse.Data.OrganizationName
                            txtDesc.text = getProfileResponse.Data.Description
                            txtDataPurge.text = getProfileResponse.Data.Datapurge
                            txtWebsite.text = getProfileResponse.Data.website
                            txtCountry.text = getProfileResponse.Data.CountryName
                            txtState.text = getProfileResponse.Data.StateName
                            txtCity.text = getProfileResponse.Data.city
                            txtZipCode.text = getProfileResponse.Data.ZipCode

                            MySharedPreferences.getMySharedPreferences()!!.userImage = getProfileResponse.Data.ProfileImage

                            MySharedPreferences.getMySharedPreferences()!!.city = getProfileResponse.Data.city

                            Glide.with(requireActivity())
                                .load(getProfileResponse.Data.ProfileImage)
                                .into(ivProfile)

                            MySharedPreferences.getMySharedPreferences()!!.userName = getProfileResponse.Data.Name

                        } else {
                            AppUtils.showToast(requireActivity(), getProfileResponse.Message)
                        }
                    } else {
                        if (isAdded) {
                            AppUtils.showToast(requireActivity(), response.message())
                        }
                    }
                }

                override fun onFailure(call: Call<GetProfileResponse?>, t: Throwable) {
                    hideProgressDialog()
                    if (isAdded) {
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