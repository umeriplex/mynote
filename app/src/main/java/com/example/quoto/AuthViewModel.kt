package com.example.quoto

import android.text.TextUtils
import androidx.appcompat.widget.ThemedSpinnerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoto.models.UserRequest
import com.example.quoto.models.UserResponse
import com.example.quoto.repo.UserRepository
import com.example.quoto.utils.Helper
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor( private val userRepository: UserRepository) : ViewModel() {

    val userLivedata : LiveData<NetworkResult<UserResponse>>
    get() = userRepository.userLiveData

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.resUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredentials(emailAddress: String, userName: String, password: String,
                            isLogin: Boolean) : Pair<Boolean, String> {

        var result = Pair(true, "")
        if(TextUtils.isEmpty(emailAddress) || (!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please provide the credentials")
        }
        else if(!Helper.isValidEmail(emailAddress)){
            result = Pair(false, "Email is invalid")
        }
        else if(!TextUtils.isEmpty(password) && password.length <= 5){
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }
}