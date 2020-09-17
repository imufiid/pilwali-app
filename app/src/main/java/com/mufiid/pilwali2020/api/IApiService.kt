package com.mufiid.pilwali2020.api

import com.mufiid.pilwali2020.models.User
import com.mufiid.pilwali2020.responses.MessageResponse
import com.mufiid.pilwali2020.responses.WrappedResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.Field
import retrofit2.http.POST

interface IApiService {

    // LOGIN
    @POST("")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Flowable<WrappedResponse<User>>
}