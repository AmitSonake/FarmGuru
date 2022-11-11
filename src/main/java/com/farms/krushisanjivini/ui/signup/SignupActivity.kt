package com.farms.krushisanjivini.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.farms.krushisanjivini.databinding.ActivitySignupBinding
import com.farms.krushisanjivini.login.LoginActivity
import com.farms.krushisanjivini.network.ApiClient
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.CheckInternetConnection
import com.farms.krushisanjivini.utilities.CheckInternetConnection.showAlertDialog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var mApiService: ApiServiceInterface?= null
    var isAllFieldFilled :Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.root)
        mApiService = ApiClient.getSignUpClient()!!.create(ApiServiceInterface::class.java)
        binding.btnLinkToLogIn.setOnClickListener {
            val intent = Intent(it.context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            it.context.startActivity(intent)
            finish()
        }
        binding.btRegistrarse.setOnClickListener {
            var networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
            if(networkStatus){
                if(checkFieldValidation()){
                    if(isValidEmail(binding.registerEmail.text.toString())){
                        userSignUp()
                    }else{
                        showAlertDialog("Please enter valid email id",this)
                    }
                }
                else
                    showAlertDialog("Please choose all fields",this)
            }else{
                CheckInternetConnection.showAlertDialog(resources.getString(com.farms.krushisanjivini.R.string.network_info),this)
                finish()
            }

        }

    }


    private fun userSignUp() {
        binding.progressbar.visibility = View.VISIBLE
        //var questionText:String? = binding.commentsText.text.toString()
        val username:String?= binding.registerName.text.toString()
        val userEmail:String?="sanjivinikrushi@gmail.com" /*binding.registerEmail.text.toString()*/
        val userPassword:String?= binding.registerPassword.text.toString()
        val mobileNo:String?= binding.mobileno.text.toString()

        mApiService!!.registerUser(username!!,userEmail!!,mobileNo!!,userPassword!!,userPassword!!).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200 ||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    println("$stringResponse")
                    CheckInternetConnection.showRegisteredDialog("User sucessfully resgistered!! Please, try to login!!",this@SignupActivity)
                }else if(response.code()==401){
                    CheckInternetConnection.showSessionTimeOutDialog("User session expired!!.",this@SignupActivity)
                }else{

                }
                binding.progressbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println(t.message)
            }
        })

    }
    private fun checkFieldValidation(): Boolean {
        return !(binding.registerName.text.toString().isNullOrEmpty()||
                /*(binding.registerEmail.text.isNullOrEmpty()||*/
                        binding.registerPassword.text.isNullOrEmpty()||
                        binding.confirmPassword.text.isNullOrEmpty()||
                        binding.mobileno.text.isNullOrEmpty()
                )
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}

