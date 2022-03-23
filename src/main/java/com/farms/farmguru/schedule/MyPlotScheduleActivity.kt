package com.farms.farmguru.schedule

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.R
import com.farms.farmguru.adapters.PlotScheduleAdapter
import com.farms.farmguru.adapters.SelectCropAdapter
import com.farms.farmguru.databinding.ActivityMyPlotBinding
import com.farms.farmguru.model.CropResponse
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPlotScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPlotBinding
    private var mApiService: ApiServiceInterface?= null
    private val cropList = ArrayList<CropResponse>()
    private var cropParsedList = ArrayList<CropResponse>()
    var languageCode:String? ="kn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPlotBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("LanguageCode") // 1
            languageCode=message
            println("language Code = $message")

        }
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        //getCropList()
        var networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus){
            getCropList()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
            finish()
        }
        // initialize grid layout manager
        GridLayoutManager(
            this, // context
            2, // span count
            RecyclerView.VERTICAL, // orientation
            false // reverse layout
        ).apply {
            // specify the layout manager for recycler view
            binding.cropRecyclerView.layoutManager=this
        }

        // finally, data bind the recycler view with adapter
     //   binding.cropRecyclerView.adapter = SelectCropAdapter(crops)


    }


    private fun getCropList(){
        binding.progressbar.visibility = View.VISIBLE
            mApiService!!.getCrops().enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        println("$stringResponse")
                        cropParsedList= stringResponse?.let { parseJson(it.toString()) }!!
                        if(cropParsedList.size>0){
                            binding.cropRecyclerView.adapter = PlotScheduleAdapter(cropParsedList)

                        }
                    }
                    binding.progressbar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println(t.message)
                }
            })

    }

    private fun parseJson(jsonResponse: String):ArrayList<CropResponse>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val cropId =jsonObj.optString("CropId")
            val cropName =jsonObj.optString("CropName")
            cropList.add(CropResponse(cropId,cropName))

        }

        return cropList
    }
}