package com.farms.krushisanjivini.ui.language

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.MainActivity
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.databinding.ActivityLanguagesBinding
import com.farms.krushisanjivini.model.Language
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLanguagesBinding
    private var mApiService: ApiServiceInterface?= null
    var context: Context? = null
    private var langaugeCode="EN"
    private var langList = ArrayList<Language>()
    private val langParsedList = ArrayList<Language>()

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
        binding = ActivityLanguagesBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val view =binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        val networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
        if(networkStatus){
            getLanguageList()
        }else{
            CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
            finish()
        }
        binding.submitButton.isEnabled=false
        binding.submitButton.setOnClickListener {

            context=it.context
            var langaugeId: String? =SharedPreferencesHelper.invoke(this).getUserLanguage()
            SharedPreferencesHelper.invoke(this).saveSelectedLanguage(langaugeCode)
            SharedPreferencesHelper.invoke(this).saveUserLoggedIn(true)
            val intent = Intent(it.context,MainActivity::class.java)
            intent.putExtra("LanguageCode",langaugeCode)
            startActivity(intent)
            finish()
        }
        binding.buttonEnglish.setOnClickListener {
            binding.buttonEnglish.setBackgroundColor(Color.parseColor("#6200EA"))
            binding.buttonEnglish.setTextColor(Color.parseColor("#FFFFFF"))
            binding.buttonKannada.setTextColor(Color.parseColor("#000000"))
            binding.buttonKannada.setBackgroundColor(Color.parseColor("#AAAAAA"))
            binding.submitButton.setBackgroundColor(Color.parseColor("#6200EA"))
            binding.submitButton.setTextColor(Color.parseColor("#FFFFFF"))
            langaugeCode= "EN"
            ///SharedPreferencesHelper.invoke(this).saveUserLanguage(langList.get(0).LangId)
            binding.submitButton.isEnabled=true
        }
        binding.buttonKannada.setOnClickListener {
            langaugeCode= "kn"
            binding.buttonKannada.setBackgroundColor(Color.parseColor("#6200EA"))
            binding.buttonKannada.setTextColor(Color.parseColor("#FFFFFF"))
            binding.buttonEnglish.setTextColor(Color.parseColor("#000000"))
            binding.buttonEnglish.setBackgroundColor(Color.parseColor("#AAAAAA"))
            binding.submitButton.setBackgroundColor(Color.parseColor("#6200EA"))
            binding.submitButton.setTextColor(Color.parseColor("#FFFFFF"))
           // SharedPreferencesHelper.invoke(this).saveUserLanguage(langList.get(1).LangId)
            binding.submitButton.isEnabled=true
        }

        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            binding.chooseLanguageLabel.text="ನಿಮ್ಮ ಭಾಷೆಯನ್ನು ಆಯ್ಕೆ ಮಾಡಿ"
            binding.submitButton.text="ಸಲ್ಲಿಸಿ"
        }else{
            binding.chooseLanguageLabel.text="Choose your language"
            binding.submitButton.text="Submit"
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

                }else if(response.code()==401){
                    CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@LanguageSelectionActivity)
                }else{

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
        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.optJSONObject(i)
            val langId =jsonObj.optString("LangId")
            val langName =jsonObj.optString("LanguageName")
            langParsedList.add(Language(langId,langName))

        }

        return langParsedList
    }

}