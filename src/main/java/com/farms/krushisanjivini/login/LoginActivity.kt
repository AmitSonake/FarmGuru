package com.farms.krushisanjivini.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.databinding.ActivityLoginBinding
import com.farms.krushisanjivini.model.CurrentUserDetails
import com.farms.krushisanjivini.model.TokenRequest
import com.farms.krushisanjivini.model.TokenResponse
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.ui.language.LanguageSelectionActivity
import com.farms.krushisanjivini.ui.signup.SignupActivity
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import com.farms.krushisanjivini.utilities.util.getProgressDrawable
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
    private var userLanguageID:Int=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.root)
        val progressDrawable =getProgressDrawable(this)
        /*binding.SplashScreenImage.loadImage(
            "https://firebasestorage.googleapis.com/v0/b/krushisanjivini-96d72.appspot.com/o/crop_images%2F48x48.png?alt=media&token=9792579c-8ecd-4b38-92bc-6c1b23ae979a", progressDrawable)*/
        if(SharedPreferencesHelper.invoke(this).getUserName().toString()!=null){
           var userID=SharedPreferencesHelper.invoke(this).getUserName().toString()
            if(userID.equals("null"))binding.loginEmail.text="".toEditable()
            else binding.loginEmail.text=userID.toEditable()
        }
        if(SharedPreferencesHelper.invoke(this).getUserPassword().toString()!=null){
            var userPassword=SharedPreferencesHelper.invoke(this).getUserPassword().toString()
            if(userPassword.equals("null"))binding.loginPassword.text="".toEditable()
            else binding.loginPassword.text=userPassword.toEditable()
        }
        SharedPreferencesHelper.invoke(this).saveIsUserPlotActive(false)
        if(SharedPreferencesHelper.invoke(this).getUserPassword()!=null){
            //binding.loginPassword.text=SharedPreferencesHelper.invoke(this).getUserPassword().toString()
        }
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            userLanguageID =2
        }else {
            userLanguageID =1
        }
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

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

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
               //binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }


    private fun getPlotList(){
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getPlotRegistered(userLanguageID).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    if(stringResponse.isNullOrEmpty()|| stringResponse.equals("[]")){
                        //navigateToActivity(1)
                    }else{
                        //navigateToActivity(2)
                    }
                    /*plotParsedList= stringResponse?.let { parseJson(it) }!!
                    if(plotParsedList.size>0){
                        binding.plotsListRecyclerView.adapter = PlotListAdapter(plotParsedList)

                    }*/
                }else{
                    CheckInternetConnection.showAlertDialog("Opss!! something went wrong please try again.",this@LoginActivity)
                }

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
        SharedPreferencesHelper.invoke(this).saveUserName(userName)
        SharedPreferencesHelper.invoke(this).saveUserPassword( password)
       // SharedPreferencesHelper.invoke(this).saveUserLoggedIn(true)
        mApiService = ApiClient.getClientRequest(SharedPreferencesHelper.invoke(this).getToken())!!.create(ApiServiceInterface::class.java)

    // getPlotList()
        fetchUserDetails()

    }

    private fun navigateToActivity(){
        binding.progressbar.visibility = View.GONE
        val intent = Intent(this,LanguageSelectionActivity::class.java)
        startActivity(intent)
    }

    private fun fetchUserDetails(){
        //binding.progressbar.visibility = View.VISIBLE
        mApiService!!.getAppCurrentUserDetails(
        ).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    val topic = Gson().fromJson(stringResponse, CurrentUserDetails::class.java)
                    //currentAppVersion = topic.VersionNumber?.get(0).toString()
                    SharedPreferencesHelper.invoke(this@LoginActivity).saveUserName( topic.UserName?.toString())
                    SharedPreferencesHelper.invoke(this@LoginActivity).saveUserEmail( topic.Email?.toString())
                    SharedPreferencesHelper.invoke(this@LoginActivity).saveUserPhoneNo( topic.PhoneNumber?.toString())
                    navigateToActivity()
                }else if(response.code()==401){
                    binding.progressbar.visibility = View.GONE
                    CheckInternetConnection.showSessionTimeOutDialog("User login session expired!!.",this@LoginActivity)
                }else{
                    binding.progressbar.visibility = View.GONE
                }
                //binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })


    }
}