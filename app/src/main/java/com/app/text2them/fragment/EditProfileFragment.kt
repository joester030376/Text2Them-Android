package com.app.text2them.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import com.app.text2them.models.ProfileModel.ProfileImageRes
import com.app.text2them.models.StateModel.State
import com.app.text2them.models.StateModel.StateResponse
import com.app.text2them.utils.AppUtils
import com.app.text2them.utils.MySharedPreferences
import com.bumptech.glide.Glide
import com.smartparking.app.rest.RetrofitRestClient
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.SocketTimeoutException
import java.util.*

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

        if (MySharedPreferences.getMySharedPreferences()!!.loginType == "3") {
            layout_Org.visibility = View.GONE
            layoutDesc.visibility = View.GONE
            layoutWebSite.visibility = View.GONE
            layoutDataPurge.visibility = View.GONE
            layoutCountry.visibility = View.GONE
            layoutState.visibility = View.GONE
            layoutCity.visibility = View.GONE
            layoutZipcode.visibility = View.GONE
            viewOrg.visibility = View.GONE
            viewDesc.visibility = View.GONE
            viewWebsite.visibility = View.GONE
            viewDataPurge.visibility = View.GONE
            viewCountry.visibility = View.GONE
            viewState.visibility = View.GONE
            viewCity.visibility = View.GONE
            viewZipcode.visibility = View.GONE
        }

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
            countryName = data.CountryName
            stateName = data.StateName

            Glide.with(requireActivity())
                .load(data.ProfileImage)
                .into(ivProfile)
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

        ivProfile.setOnClickListener {
            selectImage()
        }
    }

    private fun validation(): Boolean {
        when {
            TextUtils.isEmpty(edtFName.text.toString()) -> {
//                Toast.makeText(requireContext(), "Please enter first name", Toast.LENGTH_SHORT)
//                    .show()
                edtFName.error = "Please enter first name"
                return false
            }
            TextUtils.isEmpty(edtLname.text.toString()) -> {
//                Toast.makeText(requireContext(), "Please enter last name", Toast.LENGTH_SHORT)
//                    .show()
                edtLname.error = "Please enter last name"
                return false
            }
            TextUtils.isEmpty(edtEmail.text.toString()) -> {
//                Toast.makeText(requireContext(), "Please enter email", Toast.LENGTH_SHORT)
//                    .show()
                edtEmail.error = "Please enter email"
                return false
            }
//            TextUtils.isEmpty(edtPassword.text.toString()) -> {
//                Toast.makeText(requireContext(), "Please enter password", Toast.LENGTH_SHORT)
//                    .show()
//                edtPassword.error= "Please enter password"
//                return false
//            }
            TextUtils.isEmpty(edtOrgName.text.toString()) -> {
//                Toast.makeText(
//                    requireContext(),
//                    "Please enter organization name",
//                    Toast.LENGTH_SHORT
//                ).show()
                edtOrgName.error = "Please enter organization name"
                return false
            }
            TextUtils.isEmpty(edtDesc.text.toString()) -> {
//                Toast.makeText(requireContext(), "Please enter description", Toast.LENGTH_SHORT)
//                    .show()
                edtDesc.error = "Please enter description"
                return false
            }
            TextUtils.isEmpty(edtWebsite.text.toString()) -> {
//                Toast.makeText(requireContext(), "Please enter website", Toast.LENGTH_SHORT)
//                    .show()
                edtWebsite.error = "Please enter website"
                return false
            }
            !Patterns.WEB_URL.matcher(AppUtils.getText(edtWebsite)).matches() -> {
//                Toast.makeText(requireContext(), "Please enter valid website", Toast.LENGTH_SHORT)
//                    .show()
                edtWebsite.error = "Please enter valid website"
                return false
            }
            TextUtils.isEmpty(edtDataPurge.text.toString()) -> {
//                Toast.makeText(requireContext(), "Please enter data purge", Toast.LENGTH_SHORT)
//                    .show()
                edtDataPurge.error = "Please enter data purge"
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
//                Toast.makeText(requireContext(), "Please enter city", Toast.LENGTH_SHORT)
//                    .show()
                edtCity.error = "Please enter city"
                return false
            }
            TextUtils.isEmpty(edtZipCode.text.toString()) -> {
//                Toast.makeText(requireContext(), "Please enter zip code", Toast.LENGTH_SHORT)
//                    .show()
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
                                StateAdapter(activity, stateList, false)
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
                MySharedPreferences.getMySharedPreferences()!!.loginType!!.toInt(),
                MySharedPreferences.getMySharedPreferences()!!.userId!!.toInt(),
                countryId.toInt(),
                AppUtils.getText(edtDataPurge),
                AppUtils.getText(edtDesc),
                data.OrganizationId,
                AppUtils.getText(edtOrgName),
                AppUtils.getText(edtZipCode),
                AppUtils.getText(edtCity),
                stateId.toInt(),
                AppUtils.getText(edtWebsite),
            )
            println(profileParam)

            val call: Call<ProfileResponse?>? =
                RetrofitRestClient.getInstance()?.editProfileDetailsApi(profileParam)

            call?.enqueue(object : Callback<ProfileResponse?> {
                override fun onResponse(
                    call: Call<ProfileResponse?>,
                    response: Response<ProfileResponse?>
                ) {
                    hideProgressDialog()
                    val getProfileResponse: ProfileResponse = response.body()!!
                    if (response.isSuccessful) {
                        if (getProfileResponse.Status) {
                            AppUtils.showToast(requireActivity(), getProfileResponse.Message)
                            val fragment = ProfileFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                                .commit()
                            //editOrganizationApi(getProfileResponse.Message)
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

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
        private const val PERMISSION_CODE = 1000
        private const val PICK_CAMERA_REQUEST = 1001
    }

    private var userChoosenTask = " "
    private var picturePath: String? = ""

    private fun selectImage() {
        val items = arrayOf<CharSequence>(
            getString(R.string.take_photo), getString(R.string.choose_library), getString(
                R.string.cancel
            )
        )
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.add_photo))
        builder.setItems(items) { dialog, item ->
            if (items[item] == getString(R.string.take_photo)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    userChoosenTask = "Take Photo"
                    if (requireActivity().checkSelfPermission(Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED ||
                        requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED ||
                        requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED
                    ) {
                        //permission was not enabled
                        val permission = arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        //show popup to request permission
                        requestPermissions(permission, PERMISSION_CODE)
                    } else {
                        openCamera()
                    }
                } else {
                    openCamera()
                }
            } else if (items[item] == getString(R.string.choose_library)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    userChoosenTask = "Choose from Library"
                    pickImage()
                } else {
                    pickImage()
                }
            } else if (items[item] == getString(R.string.cancel)) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, PICK_CAMERA_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    openCamera()
                } else {
                    //permission from popup was denied
                    Toast.makeText(requireActivity(), "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, PICK_IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //val finalFile = File(getRealPathFromURI(tempUri))
            val extras = data!!.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            val tempUri: Uri = getImageUri(requireActivity(), imageBitmap)!!
            picturePath = getRealPathFromURI(tempUri)
            ivProfile.setImageURI(tempUri)
            profileUploadApi()
        } else if (requestCode == PICK_IMAGE_REQUEST_CODE
            && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            val uri: Uri = data.data!!
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor =
                activity!!.contentResolver.query(
                    uri,
                    projection,
                    null,
                    null,
                    null
                )!!
            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(projection[0])
            picturePath = cursor.getString(columnIndex) // returns null
            cursor.close()
            ivProfile.setImageURI(uri)
            profileUploadApi()
        }
    }


    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        val OutImage = Bitmap.createScaledBitmap(inImage!!, 1000, 1000, true)
        val path =
            MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                OutImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri: Uri?): String? {
        var path = ""
        if (requireActivity().contentResolver != null) {
            val cursor: Cursor =
                requireActivity().contentResolver.query(
                    uri!!,
                    null,
                    null,
                    null,
                    null
                )!!
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun profileUploadApi() {
        if (AppUtils.isConnectedToInternet(activity)) {
            showProgressDialog(requireActivity())
            val params = HashMap<String, RequestBody?>()
            params["UserId"] =
                AppUtils.getRequestBody(MySharedPreferences.getMySharedPreferences()!!.userId)

            var partbody1: MultipartBody.Part? = null
            if (picturePath!!.isNotEmpty()) {
                val file = File(picturePath)
                val reqFile1 = file.asRequestBody("image/*".toMediaTypeOrNull())
                partbody1 = MultipartBody.Part.createFormData("request", file.name, reqFile1)
            }

            val call: Call<ProfileImageRes?>? =
                RetrofitRestClient.getInstance()?.profileUploadApi(params, partbody1)
            call?.enqueue(object : Callback<ProfileImageRes?> {
                override fun onResponse(
                    call: Call<ProfileImageRes?>,
                    response: Response<ProfileImageRes?>
                ) {
                    hideProgressDialog()
                    if (response.isSuccessful) {
                        val imageResponse: ProfileImageRes = response.body()!!
                        if (imageResponse.Status) {
                            Toast.makeText(
                                requireActivity(),
                                imageResponse.Message,
                                Toast.LENGTH_SHORT
                            ).show()
                            val fragment = ProfileFragment()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                                .commit()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                imageResponse.Message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProfileImageRes?>, t: Throwable) {
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