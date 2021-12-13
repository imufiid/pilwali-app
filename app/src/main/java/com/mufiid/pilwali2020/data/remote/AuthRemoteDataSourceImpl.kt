package com.mufiid.pilwali2020.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mufiid.pilwali2020.data.entity.User
import com.mufiid.pilwali2020.data.remote.api.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) : AuthRemoteDataSource {
    override suspend fun login(userName: String, password: String): LiveData<User?> {
        val resultResponse = apiService.loggedIn(userName, password)
        val result = MutableLiveData<User?>()
        if (resultResponse.status == 200) {
            if (resultResponse.data != null) {
                result.postValue(resultResponse.data)
            }
        }
        return result
    }

    override suspend fun getUserByID(userID: Int): LiveData<User?> {
        val resultResponse = apiService.getUserByID(userID.toString())
        val result = MutableLiveData<User?>()
        if (resultResponse.status == 200) {
            if (resultResponse.data != null) {
                result.postValue(resultResponse.data)
            }
        }
        return result
    }

    override suspend fun updateProfile(
        userID: RequestBody?,
        userName: RequestBody?,
        name: RequestBody?,
        pict: MultipartBody.Part?,
        newPassword: RequestBody?,
        apiKey: RequestBody?
    ): LiveData<String> {
        val resultResponse =
            apiService.updateUser(userID, userName, name, pict, newPassword, apiKey)
        val result = MutableLiveData<String>()
        if (resultResponse.status == 200) {
            result.postValue(resultResponse.message)
        }
        return result
    }
}