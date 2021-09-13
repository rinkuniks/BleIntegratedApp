package com.technoidentity.vitalz.data.network

import android.content.Context
import android.util.Log
import com.technoidentity.vitalz.data.network.Urls.BASE_URL
import com.technoidentity.vitalz.home.HomeActivity
import com.technoidentity.vitalz.utils.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

object VitalzService {

    private var restApi: VitalzApi? = null
    var token = String()

    fun getRestApi(context: Context? = null): VitalzApi? {
        context?.let {
            val sp = it.getSharedPreferences(Constants.PREFERENCE_NAME , Context.MODE_PRIVATE)
            token = sp.getString(Constants.TOKEN , token)!!
        }

        if (restApi!= null) {
            restApi = null

        }
        init()
        return restApi
    }

    private fun init() {
        val interceptor = HttpLoggingInterceptor()

        val errorInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request = chain.request()
                val builder: Request.Builder = if (token.isEmpty()) {
                    request.newBuilder()
                } else {
                    request.newBuilder().header("Authorization", String.format("Bearer %s", token))
                }
                val request1 = builder.build()
                val response = chain.proceed(request1)
                return when (response.code) {
                    500 -> {
                        //throw Exception("Server error code: " + response.code + " with error message: " + response.message)
                        response
                        try {
                            Response.Builder().message(response.message).build()
                        } catch (e: Exception) {
                            Response.Builder().message(e.localizedMessage).build()
                        } finally {
                            Response.Builder().message("Server not responding").build()
                        }
                    }
                    401 -> {
                        //throw Exception("Server error code: " + response.code + " with error message: " + response.message)
                        response

                    }
                    400 -> {
                        //throw Exception("Server error code: " + response.code + " with error message: " + response.message)
                        response
                    }
                    200 -> {
                        response
                    }
                    else -> {
                        //throw Exception("Server error code: " + response.code + " with error message: " + response.message)
                        response
                    }
                }
            }
        }

        val headerAuthorizationInterceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request = chain.request()
                val builder: Request.Builder = if (token.isEmpty()) {
                    request.newBuilder()
                } else {
                    request.newBuilder().header("Authorization", String.format("Bearer %s", token))
                }
                val request1 = builder.build()
                return chain.proceed(request1)
            }
        }

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(headerAuthorizationInterceptor).addInterceptor(errorInterceptor)
            .addInterceptor(interceptor).connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(client).addConverterFactory(GsonConverterFactory.create()).build()

        restApi = retrofit.create(VitalzApi::class.java)
    }
}