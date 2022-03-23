package com.farms.farmguru.ui.weather

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.view.View
import com.farms.farmguru.databinding.ActivityMainBinding
import com.farms.farmguru.databinding.ActivityWeatherBinding
import org.json.JSONObject
import java.net.URL
import java.nio.charset.Charset

import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    val CITY:String ="Pune"
    val API:String ="96e8738a12275328d71b65f5341fb76b"
                     //96e8738a12275328d71b65f5341fb76b

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherTask().execute()
    }
    inner class weatherTask(): AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg p0: String?): String? {
           var response:String?
           try{
               var url:String? ="https://api.openweathermap.org/data/2.5/weather?q=Pune&appid=96e8738a12275328d71b65f5341fb76b"
               response =  URL(url).readText(Charsets.UTF_8)

           }catch (e:Exception){
                response = null
           }
            return response

        }

        override fun onPreExecute() {
            super.onPreExecute()
            binding.loader.visibility = View.VISIBLE
            binding.mainContainer.visibility = View.GONE
            binding.errortext.visibility = View.GONE
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try{
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt = jsonObj.getLong("dt")
                val updatedText = "Updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.ENGLISH).format(Date( updatedAt*1000))//format(Date( updatedAt*1000))
                val temp = main.getString("temp")+"oC"
                val tempMin ="Min Temp: "+main.getString("temp_min")+"oC"
                val tempMax ="Max Temp: "+main.getString("temp_max")+"oC"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong ("sunrise")
                val sunset:Long = sys.getLong ("sunset")
                val windspeed= wind.getString("speed")
                val weatherDescription= weather.getString("description")
               // println("description = $weatherDescription)
                val address = jsonObj.getString("name")+", "+sys.getString("country")

                binding.address.text=address
                binding.updatedAt.text=updatedText
                binding.status.text=weatherDescription.capitalize()
                binding.temp.text=temp
                binding.tempMin.text=tempMin
                binding.tempMax.text=tempMax
                binding.sunrise.text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunrise*1000))
                binding.sunset.text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunset*1000))
                binding.wind.text=windspeed
                binding.pressure.text=pressure
                binding.humidity.text=humidity
                binding.loader.visibility =View.GONE
                binding.mainContainer.visibility =View.VISIBLE


            }catch (e:Exception){
                e.printStackTrace()
                binding.loader.visibility =View.GONE
                binding.errortext.visibility =View.VISIBLE

            }
        }

    }
}