package com.ahmed.english_pl.retrofit

import com.ahmed.english_pl.utils.Constants.Headers.X_AUTH_TOKEN
import com.ahmed.english_pl.utils.Constants.Headers.X_AUTH_TOKEN_VALUE
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
            .newBuilder()
            .addHeader(X_AUTH_TOKEN, X_AUTH_TOKEN_VALUE)
            .build()
        return chain.proceed(request)
    }
}