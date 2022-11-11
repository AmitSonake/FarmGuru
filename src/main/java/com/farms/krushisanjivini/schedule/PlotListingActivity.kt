package com.farms.krushisanjivini.schedule

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.ListSchedulePlotAdapter
import com.farms.krushisanjivini.databinding.ActivityMyPlotListingBinding
import com.farms.krushisanjivini.model.PlotListing
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
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
    private var userLanguageID:Int=1

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // back to the parent activity.
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPlotListingBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        languageCode=SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            userLanguageID =2
        }else {
            userLanguageID =1
        }
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("LanguageCode") // 1
            languageCode=message
            println("language Code = $message")

        }
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)

        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus) {
            getPlotList()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)
    }


    private fun getPlotList(){
        binding.progressbar.visibility = View.VISIBLE
            mApiService!!.getPlotRegistered(userLanguageID).enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        println("$stringResponse")
                        plotParsedList= stringResponse?.let { parseJson(it) }!!
                        if(plotParsedList.size>0){
                            binding.plotsListRecyclerView.adapter = ListSchedulePlotAdapter(plotParsedList,languageCode)
                        }else{
                            if(userLanguageID==2){
                                binding.noScheduleLabel.text="ಯಾವುದೇ ಡೇಟಾ ಲಭ್ಯವಿಲ್ಲ"
                            }else{
                                binding.noScheduleLabel.text="There is no any data available"
                            }
                            binding.noScheduleLabel.visibility = View.VISIBLE
                        }
                    }else if(response.code()==401){
                        CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@PlotListingActivity)
                    }else{

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
        for (i in 0..jsonArray.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val regId =jsonObj.optInt("RegId")
            val plotId =jsonObj.optString("PlotId")
            val farmName =jsonObj.optString("FarmerName")
            val farmAddress =jsonObj.optString("FarmerAddress")
            val farmPruningDate =jsonObj.optString("PruningDate")
            val cropSeason =jsonObj.optString("CropSeason")

            val farmTaluqa =jsonObj.optString("FarmerTal")
            val farmDistrict =jsonObj.optString("FarmerDistrict")
            val farmState =jsonObj.optString("FarmerState")

            val crop =jsonObj.optString("Crop")
            val cropVariety =jsonObj.optString("CropVariety")

            val soilType =jsonObj.optString("SoilType")

            val cropPurpose =jsonObj.optString("CropPurpose")
            val cropDistance =jsonObj.optString("CropDistance")
            val plotAge =jsonObj.optInt("PlotAge")
            val plotArea =jsonObj.optInt("PlotArea")
            val isPaid =jsonObj.optBoolean("isPaid")
            //val isTrial =jsonObj.optBoolean("isTrial")
            val regDate =jsonObj.optString("RegDate")



            plotList.add(PlotListing(regId,plotId,farmName,farmAddress,farmTaluqa,
                farmDistrict,farmState,
                crop,cropVariety,cropSeason,farmPruningDate,soilType,cropPurpose,cropDistance,
                plotAge,plotArea,isPaid))

        }

        return plotList
    }
}