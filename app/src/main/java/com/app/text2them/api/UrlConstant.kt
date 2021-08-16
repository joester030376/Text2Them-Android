package com.bmd.mybmd.api

object UrlConstant {

    const val BASE_URL = "http://text2thembackend.sigmasolve.net"


    /**
     * End Points
     */
    const val LOGIN = "/api/Registration/Login"
    const val FORGOT_PASSWORD = "/api/Registration/ForgotPasswordSendMail"
    const val CHANGE_PASSWORD = "/api/Registration/Resetpassword"
    const val GET_PROFILE = "api/Account/GetProfile"
    const val COUNTRY_LIST = "api/Account/GetCountry"
    const val STATE_LIST = "api/Account/GetState"
    const val EDIT_PROFILE_DETAILS = "api/Account/EditFullProfileDetail"
    const val EDIT_ORGANIZATION_DETAILS = "api/Account/EditOrganization"
    const val STAFF_MEMBERS_LIST = "api/StaffMember/StaffMemberList"
    const val STAFF_MEMBERS_DETAILS = "api/StaffMember/View"
    const val STAFF_MEMBERS_DELETE = "api/StaffMember/Delete"
    const val DEPARTMENT_LIST = "api/Department/DepartmentList"
    const val DELETE_DEPARTMENT = "api/Department/Delete"
    const val EDIT_DEPARTMENT = "api/Department/Edit"
    const val ADD_DEPARTMENT = "api/Department/ADD"
    const val DESIGNATION_LIST = "api/Designation/DesignationList"
    const val DELETE_DESIGNATION = "api/Designation/Delete"
    const val ADD_DESIGNATION = "api/Designation/ADD"
    const val EDIT_DESIGNATION = "api/Designation/Edit"
    const val EDIT_USER = "api/StaffMember/Edit"
    const val SAVE_USER = "api/StaffMember/StaffMemberSave"
    const val MY_PLAN = "api/Subscription/SubscriptionDetail"
    const val CHANGE_NUMBER = "api/Message/StaffmemberMoNochange"
    const val UPDATE_PROFILE_IMAGE = "api/Account/updateprofilepicture"
    const val SEND_NEW_MESSAGE = "api/Message/SendNewMessage"
    const val CONTACT_LIST = "api/Message/ContactMessageList"
    const val CHAT_LIST_TWO_WAY = "api/Message/MessageChatList"
    const val CHAT_LIST_ONE_WAY = "api/Message/SendMessageChatList"
    const val SEND_MSG = "api/Message/ReplyNewMessage"
    const val DROP_DOWN_LIST = "api/Message/ContactToUserDropDownList"
    const val MESSAGE_TO_USER_LIST = "api/Message/ContactToUserGetUsersList"
    const val SEND_MESSAGE_TO_USER_LIST = "api/Message/SendMessagetoUsers"
    const val MESSAGE_QUEUE_LIST = "api/Message/ReceiveKeywordList"
    const val MESSAGE_ACCEPT = "api/Message/AcceptKeyword"



    //Global Variable
    const val IS_LOGIN = "isLogin"
    const val PROFILE_URL = "http://text2themnew.sigmasolve.net"
    const val FIREBASE_TOKEN = "firebase_token"
    const val USER_DEVICE_ID = "user_device_id"
    const val IS_INTRO_SCREEN = "is_intro_screen"
    const val ACCESS_TOKEN = "access_token"
    const val USER_NAME = "user_name"
    const val USER_ID = "user_id"
    const val ADMIN_ID = "admin_id"
    const val USER_FULL_NAME = "user_full_name"
    const val USER_EMAIL = "user_email"
    const val USER_CONTACT_NUMBER = "user_contact_number"
    const val USER_COUNTRY_NAME = "user_country_name"
    const val USER_IMAGE = "user_image"
    const val CITY = "city"
    const val NOTIFICATION = "notification"
    const val ADDRESS = "address"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val PINSET = "pinset"
    const val PASSWORD = "password"
    const val LOGIN_TYPE = "login_type"
    var TRAIL: String = "One"
}