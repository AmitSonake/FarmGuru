package com.farms.farmguru.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.MainActivity
import com.farms.farmguru.R
import com.farms.farmguru.adapters.NewsListingAdapter
import com.farms.farmguru.adapters.spinners.PlotListAdapter
import com.farms.farmguru.databinding.ActivityLoginBinding
import com.farms.farmguru.model.NewsData
import com.farms.farmguru.model.NewsListing
import com.farms.farmguru.model.TokenRequest
import com.farms.farmguru.model.TokenResponse
import com.farms.farmguru.network.ApiClient
import com.farms.farmguru.network.ApiServiceInterface
import com.farms.farmguru.ui.language.LanguageSelectionActivity
import com.farms.farmguru.ui.signup.SignupActivity
import com.farms.farmguru.ui.splash.TestActivity
import com.farms.farmguru.utilities.CheckInternetConnection
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var mApiService: ApiServiceInterface?= null
    var userName:String?=null
    var password:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.root)

        mApiService = ApiClient.getClient()!!.create(ApiServiceInterface::class.java)
        binding.btnLinkToLogIn.setOnClickListener {

            userName =binding.loginEmail.text.toString()
            password = binding.loginPassword.text.toString()
            if(userName.isNullOrEmpty() || password.isNullOrEmpty()){
                CheckInternetConnection.showAlertDialog("Please enter username and password correctly",this)
            }else {
                var networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
                if(networkStatus){
                    fetchApiToken()
                }else {
                    CheckInternetConnection.showAlertDialog(resources.getString(R.string.network_info),this)
                }
            }


        }

        binding.btnLinkToSignup.setOnClickListener {
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun fetchApiToken(){
        binding.progressbar.visibility = View.VISIBLE
        val tokenRequest = TokenRequest(userName,password,"password")

        mApiService!!.getToken(
            userName!!,password!!,"password").enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.code() == 400) {
                    CheckInternetConnection.showAlertDialog("Invalid Username or password",this@LoginActivity)
                    response.errorBody()?.let { Log.e("error", it.string())
                        binding.progressbar.visibility = View.GONE
                    return
                    }
                }
                if(response.code()==200){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    val topic = Gson().fromJson(stringResponse, TokenResponse::class.java)
                    println("Topic = ${topic.access_token}")
                    loadActivity(topic)
                }else{
                    //CheckInternetConnection.showAlertDialog("Invalid Username or password",this@LoginActivity)
                }
               binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }


    private fun getPlotList(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getPlotRegistered().enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    if(stringResponse.isNullOrEmpty()|| stringResponse.equals("[]")){
                        navigateToActivity(1)
                    }else{
                        navigateToActivity(2)
                    }
                    /*plotParsedList= stringResponse?.let { parseJson(it) }!!
                    if(plotParsedList.size>0){
                        binding.plotsListRecyclerView.adapter = PlotListAdapter(plotParsedList)

                    }*/
                }else{
                    CheckInternetConnection.showAlertDialog("Opss!! something went wrong please try again.",this@LoginActivity)
                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }

    private fun loadActivity(topic: TokenResponse?) {
        val accesToken:String? = topic!!.access_token
        println(accesToken)
        SharedPreferencesHelper.invoke(this).saveUserToken(accesToken)
        SharedPreferencesHelper.invoke(this).saveUserName( topic!!.userName)
       // SharedPreferencesHelper.invoke(this).saveUserLoggedIn(true)
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)
        getPlotList()

    }

    private fun navigateToActivity(flag: Int){
        if(flag==1){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }else {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }


}