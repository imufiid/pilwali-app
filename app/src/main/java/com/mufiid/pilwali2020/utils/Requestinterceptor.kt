package com.mufiid.pilwali2020.utils

import com.mufiid.pilwali2020.data.entity.RequestHeaders
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class RequestInterceptor(private val requestHeaders: RequestHeaders) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val builder: Request.Builder = original.newBuilder()
            .header("Authorization", "Bearer " + requestHeaders.token)
            .method(original.method, original.body)
        val newRequest: Request = builder.build()
        return chain.proceed(newRequest)
    }
}