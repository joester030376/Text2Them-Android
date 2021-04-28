package com.bmd.mybmd.api

import com.app.text2them.models.ChnagePassword.ChangePassParam
import com.app.text2them.models.ChnagePassword.ChangePassResponse
import com.app.text2them.models.CountryModel.CountryListResponse
import com.app.text2them.models.CountryModel.CountryParam
import com.app.text2them.models.EditProfileModel.OrganizationParam
import com.app.text2them.models.EditProfileModel.ProfileDetailsParam
import com.app.text2them.models.EditProfileModel.ProfileResponse
import com.app.text2them.models.ForgotPassword.ForgotPassParam
import com.app.text2them.models.ForgotPassword.ForgotPassRes
import com.app.text2them.models.GetProfile.GetProfileParam
import com.app.text2them.models.GetProfile.GetProfileResponse
import com.app.text2them.models.LoginModel.LoginParam
import com.app.text2them.models.LoginModel.LoginResponse
import com.app.text2them.models.StateModel.StateResponse
import com.app.text2them.models.UserListModel.UserListParam
import com.app.text2them.models.UserListModel.UserListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(UrlConstant.LOGIN)
    fun loginApi(@Body param: LoginParam?): Call<LoginResponse?>?

    @POST(UrlConstant.CHANGE_PASSWORD)
    fun changePasswordApi(@Body param: ChangePassParam?): Call<ChangePassResponse?>?

    @POST(UrlConstant.FORGOT_PASSWORD)
    fun forgotPasswordApi(@Body param: ForgotPassParam?): Call<ForgotPassRes?>?

    @POST(UrlConstant.GET_PROFILE)
    fun getProfileDataApi(@Body param: GetProfileParam?): Call<GetProfileResponse?>?

    @POST(UrlConstant.COUNTRY_LIST)
    fun getCountryListApi(@Body param: CountryParam): Call<CountryListResponse?>?

    @POST(UrlConstant.STATE_LIST)
    fun getStateListApi(@Body param: CountryParam): Call<StateResponse?>?

    @POST(UrlConstant.EDIT_PROFILE_DETAILS)
    fun editProfileDetailsApi(@Body param: ProfileDetailsParam): Call<ProfileResponse?>?

    @POST(UrlConstant.EDIT_ORGANIZATION_DETAILS)
    fun editOrgDetailsApi(@Body param: OrganizationParam): Call<ProfileResponse?>?

    @POST(UrlConstant.STAFF_MEMBERS_LIST)
    fun userListApi(@Body param: UserListParam): Call<UserListResponse?>?
}