package com.example.mvdugargroup.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mvdugargroup.Api.ApiResponse
import com.example.mvdugargroup.Api.BusinessUnit
import com.example.mvdugargroup.Api.DeleteFuelIssueRequest
import com.example.mvdugargroup.Api.FuelExistingEntry
import com.example.mvdugargroup.Api.FuelIssueRequest
import com.example.mvdugargroup.Api.FuelType
import com.example.mvdugargroup.Api.LoginDetailsResponse
import com.example.mvdugargroup.Api.MeterStatus
import com.example.mvdugargroup.Api.PrevReadingResult
import com.example.mvdugargroup.Api.StockQuantity
import com.example.mvdugargroup.Api.VehicleList
import com.example.mvdugargroup.Api.Warehouse
import com.example.mvdugargroup.Route

import com.example.mvdugargroup.network.RetrofitInstance
import com.example.mvdugargroup.sharedPreference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import kotlin.toString

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferenceManager(application)

    val selectedModule: StateFlow<String?> = prefs.selectedModuleFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val favoriteModules: StateFlow<Set<String>> = prefs.favoriteModulesFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    fun updateSelectedModule(module: String) {
        viewModelScope.launch {
            prefs.saveSelectedModule(module)
        }
    }

    fun updateFavoriteModules(modules: Set<String>) {
        viewModelScope.launch {
            prefs.saveFavoriteModules(modules)
        }
    }


    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var rememberMe by mutableStateOf(false)
        private set
    var passwordVisible by mutableStateOf(false)
        private set
    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private var _userDetails = MutableStateFlow<LoginDetailsResponse?>(null)
    val userDetails: StateFlow<LoginDetailsResponse?> = _userDetails.asStateFlow()


    init {
        viewModelScope.launch {
            prefs.rememberMeFlow.collect { remembered ->
                if (remembered) {
                    _navigateToHome.value = true
                }
            }
        }
    }

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun togglePasswordVisibility() {
        passwordVisible = !passwordVisible
    }

    fun onRememberMeChange(checked: Boolean) {
        rememberMe = checked
    }

    fun reset() {
        username = ""
        password = ""
        rememberMe = false
        passwordVisible = false
    }

    fun loadUserDetails() {
        viewModelScope.launch {
            prefs.getUserDetails().collect {
                _userDetails.value = it
            }
        }
    }

    fun onLoginSuccess() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response =
                    withContext(Dispatchers.IO) { RetrofitInstance.api.login(username, password) }
                if (response.isSuccessful && response.body()?.statusCode == 200) {
                    val userId = response.body()?.result?.id
                    Log.e("userId", "$userId")
                    prefs.saveRememberMe(rememberMe)
                    prefs.saveUser(response.body()!!.result)
                    _navigateToHome.value = true
                } else {
                    _errorMessage.value = "Login failed: ${response.code()} ${response.message()}"
                    Log.e("LoginError", "Code: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage ?: "Unknown error"}"
                Log.e("LoginException", e.message ?: "Unknown exception")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _fuelTypes = MutableLiveData<List<FuelType>>()
    val fuelTypes: LiveData<List<FuelType>> = _fuelTypes
    fun fetchFuelTypes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = withContext(Dispatchers.IO) { RetrofitInstance.api.fetchFuelTypes() }
                if (response.isSuccessful && response.body()?.statusCode == 200) {
                    val fuelTypes = response.body()?.result ?: emptyList()
                    _fuelTypes.value = fuelTypes
                    Log.d("fuelTypes", fuelTypes.toString())
                } else {
                    _fuelTypes.value = emptyList()
                    _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                    Log.e(
                        "FuelTypeError",
                        "Code: ${response.code()}, Message: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _fuelTypes.value = emptyList()
                _errorMessage.value = "Exception: ${e.localizedMessage}"
                Log.e("FuelTypeException", e.toString())
            } finally {
                fetchBusinessUnit()
                //_isLoading.value = false
            }
        }
    }
    private val _businessType = MutableLiveData<List<BusinessUnit>>()
    val businessType: LiveData<List<BusinessUnit>> = _businessType
    suspend fun fetchBusinessUnit() {

        _errorMessage.value = null
        try {
            val response = withContext(Dispatchers.IO) { RetrofitInstance.api.fetchBusinessUnit() }
            if (response.isSuccessful && response.body()?.statusCode == 200) {
                val businessUnits = response.body()?.result ?: emptyList()
                Log.d("businessUnits", businessUnits.toString())
                _businessType.value = businessUnits
            } else {
                _businessType.value = emptyList()
                _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                Log.e("businessUnits", "Code: ${response.code()}, Message: ${response.message()}")
            }
        } catch (e: Exception) {
            _businessType.value = emptyList()
            _errorMessage.value = "Exception: ${e.localizedMessage}"
            Log.e("businessUnits", "Exception $e")
        } finally {
            _isLoading.value = false
        }
    }

    private val _warehouse = MutableLiveData<List<Warehouse>>()
    val warehouse: LiveData<List<Warehouse>> = _warehouse

    fun fetchWarehouse(
        businessUnitId: Int,
        fuelTypeId: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.fetchWarehouse(
                        businessUnitId,
                        fuelTypeId
                    )
                }
                if (response.isSuccessful && response.body()?.statusCode == 200) {
                    val warehouses = response.body()?.result ?: emptyList()
                    Log.d("Warehouse", warehouses.toString())
                    _warehouse.value = warehouses
                } else {
                    _warehouse.value = emptyList()
                    _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                    Log.e("Warehouse", "Code: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                _warehouse.value = emptyList()
                _errorMessage.value = "Exception: ${e.localizedMessage}"
                Log.e("Warehouse", "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }

    }

    private val _stockQuantity = MutableLiveData<StockQuantity?>()
    val stockQuantity: LiveData<StockQuantity?> = _stockQuantity

    fun fetchStockQuantity(
        businessUnitId: Int,
        fuelTypeId: Int,
        warehouseId: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.fetchStockQuantity(
                        businessUnitId,
                        fuelTypeId,
                        warehouseId
                    )
                }
                if (response.isSuccessful && response.body()?.statusCode == 200) {
                    val stock = response.body()?.result
                    Log.d("Stock", stock.toString())
                    _stockQuantity.value = stock
                } else {
                    _stockQuantity.value = null
                    _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                    Log.e("Stock", "Code: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                _stockQuantity.value = null
                _errorMessage.value = "Exception: ${e.localizedMessage}"
                Log.e("Stock", "Exception $e")
            } finally {
                _isLoading.value = false
            }
        }
    }


    private val _meterStatus = MutableLiveData<List<MeterStatus>>()
    val meterStatus: LiveData<List<MeterStatus>> = _meterStatus


    fun fetchMeterStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response =
                    withContext(Dispatchers.IO) { RetrofitInstance.api.fetchMeterStatus() }
                if (response.isSuccessful && response.body()?.statusCode == 200) {
                    val meterStatus = response.body()?.result
                    Log.d("Meter", meterStatus.toString())
                    _meterStatus.value = meterStatus ?: emptyList()
                } else {
                    _meterStatus.value = emptyList()
                    _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                    Log.e("Meter", "Code: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                _meterStatus.value = emptyList()
                _errorMessage.value = "Exception: ${e.localizedMessage}"
                Log.e("Meter", "Exception $e")
            } finally {
                // _isLoading.value = false
            }
        }
    }

    // Fuel Type
    val selectedFuelTypeId = mutableStateOf<Int?>(null)
    val selectedFuelTypeName = mutableStateOf<String?>(null)

    // Business Unit
    val selectedBusinessUnitId = mutableStateOf<Int?>(null)
    val selectedBusinessUnitName = mutableStateOf<String?>(null)

    // Warehouse
    val selectedWarehouseId = mutableStateOf<Int?>(null)
    val selectedWarehouseName = mutableStateOf<String?>(null)

    // Stock
    val stock = mutableStateOf<Double?>(null)

    // Vehicle Info
    val selectedVehicleName = mutableStateOf<String?>(null)
    val selectedVehicleNumber = mutableStateOf<String?>(null)
    val standardConsumption = mutableStateOf<Double?>(null)
    val previousReading = mutableStateOf<Double?>(null)
    val previousIssueDate = mutableStateOf<String?>(null)
    val meterStatusString = mutableStateOf<String?>(null)
    val currentReading = mutableStateOf<Double?>(null)
    val standardConsumptionType = mutableStateOf<String>("")

    // Entry Info
    val entryBy = mutableStateOf<String>("")  // Default
    val issueNo = mutableStateOf<String>("")       // Generate if needed
    val issueDate = mutableStateOf<String>("")     // Set via DatePicker
    val assetId = mutableStateOf<String>("")     // Set via DatePicker
    val costCenter = mutableStateOf<String>("")     // Set via DatePicker
    val issueQuanity = mutableStateOf<Double>(0.0)     // Set via DatePicker>)

    private val _formState = MutableStateFlow<FuelIssueRequest?>(null)
    val formState: StateFlow<FuelIssueRequest?> = _formState

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile: StateFlow<File?> = _imageFile

    fun updateFormData(data: FuelIssueRequest) {
        _formState.value = data
    }

    fun updateImageFile(file: File) {
        _imageFile.value = file
    }


    private val _existingFuelEntries = MutableLiveData<List<FuelExistingEntry>?>()
    val existingFuelEntries: LiveData<List<FuelExistingEntry>?> = _existingFuelEntries

    fun fetchExistingEntries(
        fromDate: String,
        toDate: String,
        itemType: String ?=null,
        buDesc: String?=null,
        whDesc: String?=null,
        vehicleName: String?=null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response =
                    withContext(Dispatchers.IO) {
                        RetrofitInstance.api.fetchExistingEntry(
                            fromDate,
                            toDate,
                            itemType,
                            buDesc,
                            whDesc,
                            vehicleName
                        )
                    }

                if (response.isSuccessful && response.body()?.isSuccess == 1) {
                    _existingFuelEntries.value = response.body()!!.result
                    Log.d("Meter", _existingFuelEntries.value.toString())

                } else {
                    _existingFuelEntries.value = null
                    _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                    Log.e(
                        "ExistingEntries",
                        "Code: ${response.code()}, Message: ${response.message()}"
                    )
                }

            } catch (e: Exception) {
                _existingFuelEntries.value = null
                _errorMessage.value = "Fetch failed:Exception ${e.message}}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    private val _vehicleList = MutableLiveData<List<VehicleList>?>()
    val vehicleList: LiveData<List<VehicleList>?> = _vehicleList

    fun fetchVehicleList(fuelTypeId: Int) {
        viewModelScope.launch {
            // _isLoading.value = true
            _errorMessage.value = null
            _previousReadingsData.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.fetchVehicleList(fuelTypeId)
                }
                if (response.isSuccessful && response.body()?.isSuccess == 1) {
                    _vehicleList.value = response.body()?.result
                    Log.d("Meter", _existingFuelEntries.value.toString())

                } else {
                    _vehicleList.value = null
                    _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                    Log.e(
                        "ExistingEntries",
                        "Code: ${response.code()}, Message: ${response.message()}"
                    )
                }

            } catch (e: Exception) {
                _vehicleList.value = null
                _errorMessage.value = "Fetch failed:Exception ${e.message}}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _previousReadingsData = MutableLiveData<PrevReadingResult?>()
    val previousReadingsData: LiveData<PrevReadingResult?> = _previousReadingsData
    fun fetchPrevReading(vehicleName: String, issueDate: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.fetchPrevReadingFetch(vehicleName, issueDate)
                }
                if (response.isSuccessful && response.body()?.isSuccess == 1) {
                    _previousReadingsData.value = response.body()?.result
                    Log.d("PrevReading", "Fetched: ${response.body()?.result}")
                } else {
                    _previousReadingsData.value = null
                    _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                _previousReadingsData.value = null
                _errorMessage.value = "Fetch failed: Exception ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun submitForm(context: Context, navController: NavController) {
        viewModelScope.launch {
            try {

                _isLoading.value = true

                val formData = _formState.value ?: return@launch
                val image = _imageFile.value

                if (image == null || !image.exists()) {
                    Log.e("TAG", "submitForm: No image selected or file not found")
                    return@launch
                }

                // Helper to convert string/double to RequestBody
                fun String.toPlainRequestBody() =
                    this.toRequestBody("text/plain".toMediaTypeOrNull())

                fun Double.toPlainRequestBody() =
                    this.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                // Multipart file part
                val imagePart = MultipartBody.Part.createFormData(
                    "File",
                    image.name,
                    image.asRequestBody("image/*".toMediaTypeOrNull())
                )

                // Make API call
                val response = RetrofitInstance.api.submitFuelIssue(
                    standardConsT = formData.standardConsumptionType.toPlainRequestBody(),
                    whDesc = formData.warehouseName.toPlainRequestBody(),
                    costCenter = formData.costCenter.toPlainRequestBody(),
                    buDesc = formData.businessUnitName.toPlainRequestBody(),
                    currentReading = formData.currentReading.toPlainRequestBody(),
                    issueDate = formData.issueDate.toPlainRequestBody(),
                    entryBy = formData.entryBy.toPlainRequestBody(),
                    assetId = formData.assetId.toPlainRequestBody(),
                    quantity = formData.issueQuanity.toPlainRequestBody(),
                    whId = formData.warehouseId.toString().toPlainRequestBody(),
                    itemType = formData.fuelTypeName.toPlainRequestBody(),
                    readUnit = formData.currentReading.toPlainRequestBody(),
                    buId = formData.businessUnitId.toString().toPlainRequestBody(),
                    itemId = formData.fuelTypeId.toString().toPlainRequestBody(),
                    standardCons = formData.standardConsumption.toPlainRequestBody(),
                    prevIssueDate = formData.previousIssueDate.toPlainRequestBody(),
                    stock = formData.stock.toPlainRequestBody(),
                    vehicleName = formData.vehicleName.toPlainRequestBody(),
                    prevReading = formData.previousReading.toPlainRequestBody(),
                    meterStatus = formData.meterStatus.toPlainRequestBody(),
                    file = imagePart,
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == 1) {
                        Toast.makeText(
                            context,
                            responseBody.result.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        clearFormData()
                        navController.navigate(Route.FUEL_ISSUE_VIEW)
                    }else{
                        Toast.makeText(
                            context,
                            "Failed to submit form:${response.body()?.errorMessages?.joinToString { "," }} \n${response.code()} ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } else {
                    val errorJson = response.errorBody()?.string() // get raw JSON string
                    var errorMessage = "Unknown error"

                    errorJson?.let {
                        try {
                            val jsonObject = JSONObject(it)
                            if (jsonObject.has("errors")) {
                                val errors = jsonObject.getJSONObject("errors")
                                val sb = StringBuilder()
                                errors.keys().forEach { key ->
                                    val messages = errors.getJSONArray(key)
                                    for (i in 0 until messages.length()) {
                                        sb.append("${messages[i]}\n")
                                    }
                                }
                                errorMessage = sb.toString()
                            } else if (jsonObject.has("title")) {
                                errorMessage = jsonObject.getString("title")
                            }
                        } catch (e: Exception) {
                            errorMessage = "Failed to parse error: ${e.message}"
                        }
                    }

                    Toast.makeText(
                        context,
                        "Failed: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()

                    Log.e("TAG", "submitForm: Failed -> $errorMessage")}
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Failed to submit form Catch: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("TAG", "submitForm: Exception -> ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun clearFormData() {
        _formState.value = null
        _imageFile.value = null
        selectedFuelTypeId.value = null
        selectedFuelTypeName.value = ""
        stock.value = 0.0
        selectedVehicleName.value = ""
        standardConsumption.value = 0.0
        previousReading.value = 0.0
        previousIssueDate.value = ""
        meterStatusString.value = ""
        currentReading.value = 0.0
        issueNo.value = ""
        issueDate.value = ""
        assetId.value = ""
        costCenter.value = ""
        issueQuanity.value = 0.0
        standardConsumptionType.value = ""
        _previousReadingsData.value = null
    }
    private val _deleteResponse = MutableLiveData<String?>()
    val deleteResponse: LiveData<String?> = _deleteResponse
    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess
    fun deleteFuelIssue(context: Context,request: DeleteFuelIssueRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteFuelIssue(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == 1) {
                        _deleteSuccess.postValue(true)
                        Toast.makeText(
                            context,
                            responseBody.result.message,
                            Toast.LENGTH_SHORT
                        ).show()
                       // _deleteResponse.postValue("Record Successfully Deleted!")
                    }else{
                        _deleteSuccess.postValue(false)
                        Toast.makeText(
                            context,
                            "Failed to delete : ${response.code()} ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }


                } else {
                    _deleteSuccess.postValue(false)
                    Toast.makeText(
                        context,
                        "Error: "+response.code()+" "+response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                    //_deleteResponse.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _deleteSuccess.postValue(false)
                Toast.makeText(
                    context,
                    "Error: "+e.message,
                    Toast.LENGTH_SHORT
                ).show()
                //_deleteResponse.postValue(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}
