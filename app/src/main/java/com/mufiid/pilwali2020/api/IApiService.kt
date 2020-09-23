package com.mufiid.pilwali2020.api

import com.mufiid.pilwali2020.models.Paslon
import com.mufiid.pilwali2020.models.Tps
import com.mufiid.pilwali2020.models.User
import com.mufiid.pilwali2020.responses.MessageResponse
import com.mufiid.pilwali2020.responses.WrappedListResponses
import com.mufiid.pilwali2020.responses.WrappedResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IApiService {

    // LOGIN
    @POST("")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Flowable<WrappedResponse<User>>

    // GET Jumlah Pemilih
    @GET("")
    fun getCountVoter(
        @Path("id_tps") id_tps: String?
    ): Flowable<WrappedResponse<Tps>>

    // GET User By Id
    @GET("")
    fun getUserById(
        @Path("id_user") id_user: String?
    ): Flowable<WrappedResponse<Tps>>

    // GET Data Paslon
    @GET("")
    fun getPaslon(): Flowable<WrappedListResponses<Paslon>>

    // GWT Data Tps
    @GET("")
    fun getDataTps(
        @Field("id_tps") id_tps: String?
    )


}