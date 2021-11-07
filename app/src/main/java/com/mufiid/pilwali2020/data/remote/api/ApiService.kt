package com.mufiid.pilwali2020.data.remote.api

import com.mufiid.pilwali2020.data.entity.Config
import com.mufiid.pilwali2020.data.entity.Tps
import com.mufiid.pilwali2020.data.entity.User
import com.mufiid.pilwali2020.data.entity.*
import com.mufiid.pilwali2020.data.remote.response.MessageResponse
import com.mufiid.pilwali2020.data.remote.response.VerificationResponse
import com.mufiid.pilwali2020.data.remote.response.WrappedListResponses
import com.mufiid.pilwali2020.data.remote.response.WrappedResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    // LOGIN
    @Deprecated("This function is not supported")
    @FormUrlEncoded
    @POST("user")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<WrappedResponse<User>>

    // LOGIN
    @FormUrlEncoded
    @POST("user")
    suspend fun loggedIn(
        @Field("username") username: String,
        @Field("password") password: String
    ): WrappedResponse<User>

    // GET Jumlah Pemilih
    @GET("")
    fun getCountVoter(
        @Path("id_tps") id_tps: String?
    ): Observable<WrappedResponse<Tps>>

    @Deprecated("This function is not supported")
    // GET User By Id
    @GET("user")
    fun getUserById(
        @Query("id") id_user: String?
    ): Observable<WrappedResponse<User>>

    @GET("user")
    fun getUserByID(
        @Query("id") id_user: String?
    ): WrappedResponse<User>

    // GET Data Paslon
    @GET("paslon")
    fun getPaslon(
        @Query("id_tps") id_tps: String?
    ): Observable<WrappedListResponses<Paslon>>

    // GET Data Tps
    @GET("tps")
    fun getDataTps(
        @Query("id") id_tps: String?
    ): Observable<WrappedResponse<Tps>>

    // GET Data Verifikasi
    @GET("perhitungan")
    fun getVerifikasi(
        @Query("verif_tps") id_tps: String?
    ): Observable<VerificationResponse>

    // GET CONFIG
    @GET("config")
    fun getConfig() : Observable<WrappedResponse<Config>>

    // INSERT DAFTAR PEMILIH
    @FormUrlEncoded
    @POST("tps")
    fun inputDaftarPemilih(
        @Field("id") id_tps: String?,
        @Field("form_page") form_page: String?,
        @Field("dpt_2") dpt: Int?,
        @Field("dptb_2") dptb: Int?,
        @Field("dpk_2") dpk: Int?,
        @Field("dpph_2") dpph: Int?,
        @Field("username") username: String?,
        @Field("api_key") api_key: String?
    ): Observable<MessageResponse>

    // INSERT DATA TPS
    @Multipart
    @POST("tps")
    fun inputDataTPS(
        @Part("id") id_tps: RequestBody?,
        @Part("form_page") form_page: RequestBody?,
        @Part foto_tps: MultipartBody.Part?,
        @Part("lati") latitude: RequestBody?,
        @Part("longi") longitude: RequestBody?,
        @Part("username") username: RequestBody?,
        @Part("api_key") apiKey: RequestBody?
    ): Observable<MessageResponse>

    // DATA SUARA PASLON
    @Multipart
    @POST("perhitungan")
    fun postSuaraPaslon(
        @Part("id_tps") id_tps: RequestBody?,
        @Part("suara_tidak_sah") suara_tidak_sah: RequestBody?,
        @Part("id_paslon[]") id_paslon: List<Int>?,
        @Part("suara_sah[]") suara_sah: List<Int>?,
        @Part foto_blanko: MultipartBody.Part?,
        @Part("username") username: RequestBody?,
        @Part("api_key") api_key: RequestBody?
    ): Observable<WrappedResponse<Tps>>

    // UPDATE PROFILE
    @Deprecated("This function is not supported")
    @Multipart
    @POST("user")
    fun updateProfile(
        @Part("id") id_user: RequestBody?,
        @Part("username") username: RequestBody?,
        @Part("nama") nama: RequestBody?,
        @Part foto: MultipartBody.Part?,
        @Part("password") passwdNew: RequestBody?,
        @Part("api_key") api_key: RequestBody?
    ): Observable<MessageResponse>

    @Multipart
    @POST("user")
    fun updateUser(
        @Part("id") userID: RequestBody?,
        @Part("username") userName: RequestBody?,
        @Part("nama") name: RequestBody?,
        @Part pict: MultipartBody.Part?,
        @Part("password") newPassword: RequestBody?,
        @Part("api_key") apiKey: RequestBody?
    ): MessageResponse
}