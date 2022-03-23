package com.farms.farmguru.ui.mydiary

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.MainActivity
import com.farms.farmguru.R
import com.farms.farmguru.adapters.NewsListingAdapter
import com.farms.farmguru.adapters.spinners.PlotListAdapter
import com.farms.farmguru.databinding.ActivityLoginBinding
import com.farms.farmguru.databinding.ActivityMydiaryBinding
import com.farms.farmguru.model.NewsData
import com.farms.farmguru.model.NewsListing
import com.farms.farmguru.model.TokenRequest
import com.farms.farmguru.model.TokenResponse
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.ui.language.LanguageSelectionActivity
import com.farms.farmguru.ui.splash.TestActivity
import com.farms.farmguru.utilities.CheckInternetConnection
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DiaryNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMydiaryBinding
    private var mApiService: ApiServiceInterface?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMydiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mApiService = ApiClient.getClient()!!.create(ApiServiceInterface::class.java)


    }




    private fun getPlotList(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getPlotRegistered().enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                }else{
                    CheckInternetConnection.showAlertDialog("Opss!! something went wrong please try again.",this@DiaryNoteActivity)
                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }


}