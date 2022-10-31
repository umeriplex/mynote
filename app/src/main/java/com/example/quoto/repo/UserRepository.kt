package com.example.quoto.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quoto.NetworkResult
import com.example.quoto.api.UserApi
import com.example.quoto.models.UserRequest
import com.example.quoto.models.UserResponse
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {
    private val mutableLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userLiveData : LiveData<NetworkResult<UserResponse>>
    get() = mutableLiveData


    suspend fun resUser(userRequest: UserRequest){
        mutableLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signUp(userRequest)
        handleResponse(response)

    }

    suspend fun loginUser(userRequest: UserRequest){
        mutableLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signIn(  userRequest)
        handleResponse(response)

    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            mutableLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            mutableLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            mutableLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}