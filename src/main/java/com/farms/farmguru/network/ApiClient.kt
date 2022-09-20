package com.farms.farmguru.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {
    val baseUrl : String?  = "http://www.krushisanjivini.com/"
    private var retrofit : Retrofit? = null;
    val weatherUrl : String?  ="https://api.weatherapi.com/"
    fun getClient():Retrofit? {
        if(retrofit==null){

            val client =OkHttpClient.Builder()
                .connectTimeout(100,TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .build()
            retrofit =Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
       return retrofit
    }

    fun getClientRequest(token:String?):Retrofit? {
       val client =  OkHttpClient.Builder()
            .addInterceptor(token?.let { OAuthInterceptor("Bearer", it) })
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit
    }

    fun getWeatherClient():Retrofit? {
        if(retrofit==null){
            val client =OkHttpClient.Builder()
                .connectTimeout(100,TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .build()
            retrofit =Retrofit.Builder()
                .client(client)
                .baseUrl(weatherUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }

}