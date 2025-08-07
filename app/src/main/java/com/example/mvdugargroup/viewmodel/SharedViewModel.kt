package com.example.mvdugargroup.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvdugargroup.Api.BusinessUnit
import com.example.mvdugargroup.Api.FuelIssueRequest
import com.example.mvdugargroup.Api.FuelType
import com.example.mvdugargroup.Api.MeterStatus
import com.example.mvdugargroup.Api.StockQuantity
import com.example.mvdugargroup.Api.Warehouse

import com.example.mvdugargroup.network.RetrofitInstance
import com.example.mvdugargroup.sharedPreference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Response
import java.io.File

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferenceManager(application)
    private val repository: FuelIssueRepository
        get() {
            return FuelIssueRepository(
                RetrofitInstance.api
            )
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
                prefs.saveUserId(userId.toString())
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
            val response = withContext(Dispatchers.IO) { RetrofitInstance.api.fetchMeterStatus() }
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
            _isLoading.value = false
        }
    }
}


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

fun submitForm() {
    viewModelScope.launch {
        try {
            val formData = _formState.value ?: return@launch
            val image = _imageFile.value

            val response = repository.sendFuelIssueRequest(formData, image)
            if (response.isSuccessful) {

            } else {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

}
