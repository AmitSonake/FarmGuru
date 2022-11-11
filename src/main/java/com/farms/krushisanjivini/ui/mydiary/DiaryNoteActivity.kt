package com.farms.krushisanjivini.ui.mydiary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.databinding.ActivityMydiaryBinding
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DiaryNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMydiaryBinding
    private var mApiService: ApiServiceInterface?= null
    private var userLanguageID:Int =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMydiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            userLanguageID =2
        }else {
            userLanguageID =1
        }

        mApiService = ApiClient.getClient()!!.create(ApiServiceInterface::class.java)


    }




    private fun getPlotList(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getPlotRegistered(userLanguageID).enqueue(object:
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