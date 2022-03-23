package com.farms.farmguru.ui.weather

import WeatherData
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.farms.farmguru.databinding.ActivityWeatherBinding
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.utilities.location.LocationTrack
import com.farms.farmguru.utilities.util.getProgressDrawable
import com.farms.farmguru.utilities.util.loadImage
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    val CITY:String ="Pune"
    val days:String ="5"
    val apiKey:String ="5e121ec6e3e549869d7142221223001"
    private var mApiService: ApiServiceInterface?= null
    private var locationLattitude:String?=null
    private var locationLongitude:String?=null
    private var userLocation:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUsersCurrentLocation()
    }

    private fun getUsersCurrentLocation() {
        if (LocationTrack(this).canGetLocation) {
            locationLattitude=LocationTrack(this).getLatitude().toString()
            locationLongitude=LocationTrack(this).getLongitude().toString()
            userLocation= "$locationLattitude,$locationLongitude"
            mApiService = ApiClient.getWeatherClient()!!.create(ApiServiceInterface::class.java)
            makeApiWeatherRequest(apiKey,userLocation,days)

            println("userLocation =$userLocation")
        }

    }

    private fun makeApiWeatherRequest(apiKey: String, userLocation: String?, days: String?) {

        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getWeatherData(apiKey!!,userLocation!!,days!!).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    val topic = Gson().fromJson(stringResponse, WeatherData::class.java)
                    val userAddress="${topic.location.name},${topic.location.region},${topic.location.country}"
                    binding.address.text=userAddress
                    binding.updatedAt.text=topic.current.last_updated
                    topic.current.condition.icon
                    binding.statusImage.loadImage(
                        topic.current.condition.icon,
                        getProgressDrawable(binding.statusImage.context))
                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })
    }


}