package com.farms.krushisanjivini.ui.videos

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.adapters.ServiceProvidersListAdapter
import com.farms.krushisanjivini.databinding.ActivityServiceProvidersListingBinding
import com.farms.krushisanjivini.model.ServiceProviders
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideosListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceProvidersListingBinding
    private var mApiService: ApiServiceInterface?= null
    private val providersList = ArrayList<ServiceProviders>()
    private var providersParsedList = ArrayList<ServiceProviders>()
    var languageCode:String? ="kn"
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
        binding = ActivityServiceProvidersListingBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
       // getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setHomeButtonEnabled(true)
        languageCode=SharedPreferencesHelper.invoke(this).getSelectedLanguage()
        val bundle :Bundle ?=intent.extras
        if (bundle!=null){
            val message = bundle.getString("LanguageCode") // 1
            languageCode=message
            println("language Code = $message")
        }
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            userLanguageID =2
            supportActionBar?.title="ಸೇವೆ ಒದಗಿಸುವವರು"
        }else {
            userLanguageID =1
        }
        GridLayoutManager(
            this, // context
            1,
            // span count
            RecyclerView.VERTICAL, // orientation
            false // reverse layout
        ).apply {
            // specify the layout manager for recycler view
            binding.providersListRecyclerView.layoutManager=this
        }
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        //getPlotList()
        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus) {
            getWebinarsList()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)

    }


    private fun getWebinarsList(){
        binding.progressbar.visibility = View.VISIBLE
            mApiService!!.getServiceProvidersByLangId(userLanguageID).enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        println("$stringResponse")
                        providersParsedList= stringResponse?.let { parseJson(it) }!!
                        if(providersParsedList.size>0){
                            binding.providersListRecyclerView.adapter = ServiceProvidersListAdapter(providersParsedList,languageCode)
                        }else{
                            if(userLanguageID==2){
                                binding.noScheduleLabel.text="ಯಾವುದೇ ಡೇಟಾ ಲಭ್ಯವಿಲ್ಲ"
                            }else{
                                binding.noScheduleLabel.text="There is no any data available"
                            }
                            binding.noScheduleLabel.visibility = View.VISIBLE
                        }
                    }else if(response.code()==401){
                        CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@VideosListingActivity)
                    }else{

                    }
                    binding.progressbar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println(t.message)
                }
            })

    }

    private fun parseJson(jsonResponse: String):ArrayList<ServiceProviders>{
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val transId =jsonObj.optInt("TransId")
            val serviceProviderId =jsonObj.optInt("ServiceProviderId")
            val langId =jsonObj.optInt("LangId")
            val providerName =jsonObj.optString("ServiceProviderName")
            val village =jsonObj.optString("Village")
            val phoneNo =jsonObj.optString("PhNo")
            val service =jsonObj.optString("Service")

            providersList.add(
                ServiceProviders(transId,serviceProviderId,langId,providerName,
            village,phoneNo,service)
            )

        }
        return providersList
    }
}