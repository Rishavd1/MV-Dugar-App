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
import com.example.mvdugargroup.Api.FuelType

import com.example.mvdugargroup.network.RetrofitInstance
import com.example.mvdugargroup.sharedPreference.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferenceManager(application)
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
                val response = RetrofitInstance.api.login(username, password)

                if (response.isSuccessful && response.body()?.statusCode == 200) {
                    val userId = response.body()?.result?.id
                    Log.e("userId", "$userId")
                    prefs.saveRememberMe(rememberMe)
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
                val response = RetrofitInstance.api.fetchFuelTypes()
                if (response.isSuccessful && response.body()?.statusCode == 200) {
                    val fuelTypes = response.body()?.result ?: emptyList()
                    _fuelTypes.value = fuelTypes
                    Log.d("fuelTypes", fuelTypes.toString())
                } else {
                    _fuelTypes.value = emptyList()
                    _errorMessage.value = "Fetch failed: ${response.code()} ${response.message()}"
                    Log.e("FuelTypeError", "Code: ${response.code()}, Message: ${response.message()}")
                }
            } catch (e: Exception) {
                _fuelTypes.value = emptyList()
                _errorMessage.value = "Exception: ${e.localizedMessage}"
                Log.e("FuelTypeException", e.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }


}
