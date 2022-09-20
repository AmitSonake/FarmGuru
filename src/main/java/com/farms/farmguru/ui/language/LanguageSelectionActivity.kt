package com.farms.farmguru.ui.language

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.MainActivity
import com.farms.farmguru.R
import com.farms.farmguru.databinding.ActivityLanguageSelectionBinding
import com.farms.farmguru.databinding.ActivityLanguagesBinding
import com.farms.farmguru.model.CropResponse
import com.farms.farmguru.model.Language
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.ui.myplots.MyPlotActivity
import com.farms.farmguru.ui.myplots.MyPlotListingActivity
import com.farms.farmguru.ui.splash.TestActivity
import com.farms.farmguru.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguagesBinding
    private var mApiService: ApiServiceInterface?= null
    var context: Context? = null
    var langaugeCode="EN"
    private var langList = ArrayList<Language>()
    private val langParsedList = ArrayList<Language>()
    val selectedlangID:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguagesBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)

        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        var networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus){
            getLanguageList()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
            finish()
        }

        binding.submitButton.setOnClickListener {
            context=it.context
            println("Selected language Id :${SharedPreferencesHelper.invoke(this).getUserLanguage()}")
            var langaugeId: String? =SharedPreferencesHelper.invoke(this).getUserLanguage()
            SharedPreferencesHelper.invoke(this).saveSelectedLanguage(langaugeCode)
            SharedPreferencesHelper.invoke(this).saveUserLoggedIn(true)
            val intent = Intent(it.context,MyPlotActivity::class.java)
            intent.putExtra("LanguageCode",langaugeCode)
            startActivity(intent)
            finish()
        }
        binding.buttonEnglish.setOnClickListener {
            binding.buttonEnglish.setBackgroundColor(Color.parseColor("#2C590F"))
            binding.buttonEnglish.setTextColor(Color.parseColor("#FFFFFF"))
            binding.buttonKannada.setBackgroundColor(Color.parseColor("#AAAAAA"))
            binding.submitButton.setBackgroundColor(Color.parseColor("#2C590F"))
            binding.submitButton.setTextColor(Color.parseColor("#FFFFFF"))
            langaugeCode= "EN"
            println("Eng selected =${langList.get(0).LangId} : ${langList.get(0).LanguageName}")
            SharedPreferencesHelper.invoke(this).saveUserLanguage(langList.get(0).LangId)
        }
        binding.buttonKannada.setOnClickListener {
            langaugeCode= "kn"
            binding.buttonKannada.setBackgroundColor(Color.parseColor("#2C590F"))
            binding.buttonKannada.setTextColor(Color.parseColor("#FFFFFF"))
            binding.buttonEnglish.setBackgroundColor(Color.parseColor("#AAAAAA"))
            binding.submitButton.setBackgroundColor(Color.parseColor("#2C590F"))
            binding.submitButton.setTextColor(Color.parseColor("#FFFFFF"))
            println("kn selected =${langList.get(1).LangId} : ${langList.get(1).LanguageName}")
            SharedPreferencesHelper.invoke(this).saveUserLanguage(langList.get(1).LangId)
        }


    }

    private fun getLanguageList(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getLanguages().enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    langList= stringResponse?.let { parseLanguageJson(it.toString()) }!!

                    if(stringResponse.isNullOrEmpty()){
                        //navigateToActivity(1)
                    }else{
                       // navigateToActivity(2)
                    }

                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }

    private fun parseLanguageJson(jsonResponse: String): ArrayList<Language> {
        val jsonArray = JSONArray(jsonResponse)
        for (i in 0..jsonArray!!.length() - 1) {
            val jsonObj = jsonArray.optJSONObject(i)
            val langId =jsonObj.optString("LangId")
            val langName =jsonObj.optString("LanguageName")
            langParsedList.add(Language(langId,langName))

        }

        return langParsedList
    }

    private fun navigateToActivity(flag: Int){
        if(flag==1){
            val intent = Intent(this,MyPlotActivity::class.java)
            intent.putExtra("LanguageCode",langaugeCode)
            startActivity(intent)
        }else {
            val intent = Intent(this, TestActivity::class.java)
            intent.putExtra("LanguageCode",langaugeCode)
            startActivity(intent)
        }
        finish()
    }
}