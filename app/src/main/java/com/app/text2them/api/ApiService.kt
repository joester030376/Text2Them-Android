package com.bmd.mybmd.api

import com.app.text2them.models.AddEditModel.AddParam
import com.app.text2them.models.AddEditModel.Add_Edit_Response
import com.app.text2them.models.AddEditModel.EditParam
import com.app.text2them.models.ChangeNumberModel.ChangeNumberParam
import com.app.text2them.models.ChangeNumberModel.ChangeNumberRes
import com.app.text2them.models.ChatModel.ChatParam
import com.app.text2them.models.ChatModel.ChatResFull
import com.app.text2them.models.ChatModelSingle.ChatParamOne
import com.app.text2them.models.ChatModelSingle.ChatResOne
import com.app.text2them.models.ChatSendMsgModel.SendMSGParam
import com.app.text2them.models.ChatSendMsgModel.SendMSGRes
import com.app.text2them.models.ChnagePassword.ChangePassParam
import com.app.text2them.models.ChnagePassword.ChangePassResponse
import com.app.text2them.models.ContactModel.ContactParam
import com.app.text2them.models.ContactModel.ContactResponse
import com.app.text2them.models.CountryModel.CountryListResponse
import com.app.text2them.models.CountryModel.CountryParam
import com.app.text2them.models.DeleteModel.Desi_Depart_Delete_Param
import com.app.text2them.models.DepartmentModel.DepartmentListParam
import com.app.text2them.models.DepartmentModel.DepartmentListRes
import com.app.text2them.models.DesignationModel.DesignationListRes
import com.app.text2them.models.DropModel.DropDownParam
import com.app.text2them.models.DropModel.DropDownRes
import com.app.text2them.models.EditProfileModel.OrganizationParam
import com.app.text2them.models.EditProfileModel.ProfileDetailsParam
import com.app.text2them.models.EditProfileModel.ProfileResponse
import com.app.text2them.models.ForgotPassword.ForgotPassParam
import com.app.text2them.models.ForgotPassword.ForgotPassRes
import com.app.text2them.models.GetProfile.GetProfileParam
import com.app.text2them.models.GetProfile.GetProfileResponse
import com.app.text2them.models.LoginModel.LoginParam
import com.app.text2them.models.LoginModel.LoginResponse
import com.app.text2them.models.MSGAcceptModel.MsgAcceptParam
import com.app.text2them.models.MSGAcceptModel.MsgAcceptRes
import com.app.text2them.models.MSGQueueModel.MSGQueueRes
import com.app.text2them.models.MSGQueueModel.MessageQueueParam
import com.app.text2them.models.MessageToUserModel.MessageToUserListRes
import com.app.text2them.models.MessageToUserModel.MessageToUserParam
import com.app.text2them.models.MessageToUserModel.SendMessageToUserParam
import com.app.text2them.models.MessageToUserModel.SendMessageToUserRes
import com.app.text2them.models.MyPlanModel.MyPlanParam
import com.app.text2them.models.MyPlanModel.MyPlanResponse
import com.app.text2them.models.ProfileModel.ProfileImageRes
import com.app.text2them.models.SendMessage.SendMessageParam
import com.app.text2them.models.SendMessage.SendMessageRes
import com.app.text2them.models.StateModel.StateResponse
import com.app.text2them.models.UserAddModel.UserAddParam
import com.app.text2them.models.UserAddModel.UserAddResponse
import com.app.text2them.models.UserDeleteModel.UserDeleteParam
import com.app.text2them.models.UserDeleteModel.UserDeleteResponse
import com.app.text2them.models.UserDetailModel.UserDetailsParam
import com.app.text2them.models.UserDetailModel.UserDetailsResponse
import com.app.text2them.models.UserEditModel.EditUserParam
import com.app.text2them.models.UserEditModel.EditUserResponse
import com.app.text2them.models.UserListModel.UserListParam
import com.app.text2them.models.UserListModel.UserListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

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

    @POST(UrlConstant.STAFF_MEMBERS_DETAILS)
    fun userDetailsApi(@Body param: UserDetailsParam): Call<UserDetailsResponse?>?

    @POST(UrlConstant.STAFF_MEMBERS_DELETE)
    fun userDeleteApi(@Body param: UserDeleteParam): Call<UserDeleteResponse?>?

    @POST(UrlConstant.DEPARTMENT_LIST)
    fun getDepartmentListApi(@Body param: DepartmentListParam): Call<DepartmentListRes?>?

    @POST(UrlConstant.DELETE_DEPARTMENT)
    fun departmentDeleteApi(@Body param: Desi_Depart_Delete_Param): Call<UserDeleteResponse?>?

    @POST(UrlConstant.ADD_DEPARTMENT)
    fun departmentAddApi(@Body param: AddParam): Call<Add_Edit_Response?>?

    @POST(UrlConstant.EDIT_DEPARTMENT)
    fun departmentEditApi(@Body param: EditParam): Call<Add_Edit_Response?>?

    @POST(UrlConstant.DESIGNATION_LIST)
    fun getDesignationListApi(@Body param: DepartmentListParam): Call<DesignationListRes?>?

    @POST(UrlConstant.DELETE_DESIGNATION)
    fun designationDeleteApi(@Body param: Desi_Depart_Delete_Param): Call<UserDeleteResponse?>?

    @POST(UrlConstant.ADD_DESIGNATION)
    fun designationAddApi(@Body param: AddParam): Call<Add_Edit_Response?>?

    @POST(UrlConstant.EDIT_DESIGNATION)
    fun designationEditApi(@Body param: EditParam): Call<Add_Edit_Response?>?

    @POST(UrlConstant.SAVE_USER)
    fun saveUserApi(@Body param: UserAddParam): Call<UserAddResponse?>?

    @POST(UrlConstant.EDIT_USER)
    fun editUserApi(@Body param: EditUserParam): Call<EditUserResponse?>?

    @POST(UrlConstant.MY_PLAN)
    fun myPlanApi(@Body param: MyPlanParam): Call<MyPlanResponse?>?

    @POST(UrlConstant.CHANGE_NUMBER)
    fun changeNumberApi(@Body param: ChangeNumberParam): Call<ChangeNumberRes?>?

    @Multipart
    @POST(UrlConstant.UPDATE_PROFILE_IMAGE)
    fun profileUploadApi(
        @PartMap params: HashMap<String, RequestBody?>, @Part partbody1: MultipartBody.Part?
    ): Call<ProfileImageRes?>?

    @POST(UrlConstant.SEND_NEW_MESSAGE)
    fun sendNewMessage(@Body param: SendMessageParam): Call<SendMessageRes?>?

    @POST(UrlConstant.CONTACT_LIST)
    fun contactList(@Body param: ContactParam): Call<ContactResponse?>?

    @POST(UrlConstant.CHAT_LIST_TWO_WAY)
    fun chatListTwoWay(@Body param: ChatParam): Call<ChatResFull?>?

    @POST(UrlConstant.CHAT_LIST_ONE_WAY)
    fun chatListOneWay(@Body param: ChatParamOne): Call<ChatResOne?>?

    @POST(UrlConstant.SEND_MSG)
    fun sendMsgApi(@Body param: SendMSGParam): Call<SendMSGRes?>?

    @POST(UrlConstant.DROP_DOWN_LIST)
    fun dropDownApi(@Body param: DropDownParam): Call<DropDownRes?>?

    @POST(UrlConstant.MESSAGE_TO_USER_LIST)
    fun messageToUserListApi(@Body param: MessageToUserParam): Call<MessageToUserListRes?>?

    @POST(UrlConstant.SEND_MESSAGE_TO_USER_LIST)
    fun sendMessageToUserListApi(@Body param: SendMessageToUserParam): Call<SendMessageToUserRes?>?

    @POST(UrlConstant.MESSAGE_QUEUE_LIST)
    fun msgQueueListApi(@Body param: MessageQueueParam): Call<MSGQueueRes?>?

    @POST(UrlConstant.MESSAGE_ACCEPT)
    fun msgAcceptApi(@Body param: MsgAcceptParam): Call<MsgAcceptRes?>?
}