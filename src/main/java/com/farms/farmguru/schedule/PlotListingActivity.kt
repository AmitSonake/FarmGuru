package com.farms.farmguru.schedule

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.adapters.ListSchedulePlotAdapter
import com.farms.farmguru.adapters.spinners.PlotListAdapter
import com.farms.farmguru.databinding.ActivityMyPlotListingBinding
import com.farms.farmguru.model.PlotListing
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlotListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPlotListingBinding
    private var mApiService: ApiServiceInterface?= null
    private val plotList = ArrayList<PlotListing>()
    private var plotParsedList = ArrayList<PlotListing>()
    var languageCode:String? ="EN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPlotListingBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        languageCode=SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("LanguageCode") // 1
            languageCode=message
            println("language Code = $message")

        }
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        getPlotList()

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)
    }


    private fun getPlotList(){
        binding.progressbar.visibility = View.VISIBLE
            mApiService!!.getPlotRegistered().enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        println("$stringResponse")
                        plotParsedList= stringResponse?.let { parseJson(it) }!!
                        if(plotParsedList.size>0){
                            binding.plotsListRecyclerView.adapter = ListSchedulePlotAdapter(plotParsedList,languageCode)

                        }
                    }
                    binding.progressbar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println(t.message)
                }
            })

    }

    private fun parseJson(jsonResponse: String):ArrayList<PlotListing>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val farmName =jsonObj.optString("FarmerName")
            val farmAddress =jsonObj.optString("FarmerAddress")
            val farmPruningDate =jsonObj.optString("PruningDate")
            val cropSeason =jsonObj.optString("CropSeason")
            val regId =jsonObj.optInt("RegId")
            val userId =jsonObj.optString("UserId")
            val farmTaluqa =jsonObj.optString("FarmerTal")
            val farmDistrict =jsonObj.optString("FarmerDistrict")
            val farmState =jsonObj.optString("FarmerState")

            val cropId =jsonObj.optInt("CropId")
            val cropVarietyId =jsonObj.optInt("CropVarietyId")
            val irrigationSourceId =jsonObj.optInt("IrrigationSourceId")
            val soilTypeId =jsonObj.optInt("SoilTypeId")

            val cropPurposeId =jsonObj.optInt("CropPurposeId")
            val cropDistance =jsonObj.optString("CropDistance")
            val plotAge =jsonObj.optInt("PlotAge")
            val plotArea =jsonObj.optInt("PlotArea")
            val isPaid =jsonObj.optBoolean("isPaid")
            val isTrial =jsonObj.optBoolean("isTrial")

            plotList.add(PlotListing(regId,userId,farmName,farmAddress,farmTaluqa,farmDistrict,farmState,
            cropId,cropVarietyId,cropSeason,farmPruningDate,irrigationSourceId,soilTypeId,cropPurposeId,cropDistance,
            plotAge,plotArea,isPaid,isTrial))

        }

        return plotList
    }
}