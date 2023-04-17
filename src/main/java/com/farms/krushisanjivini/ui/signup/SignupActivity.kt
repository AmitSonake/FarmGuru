package com.farms.krushisanjivini.ui.signup

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
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
import android.text.Editable

import android.text.TextWatcher





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
                    if(testPassword())
                    userSignUp()
                    else
                        showAlertDialog("Please enter atleast 6 char for Pin also make sure pin and confirm pin should match",this)
                }
                else
                    showAlertDialog("Please choose all fields",this)
            }else{
                CheckInternetConnection.showAlertDialog(resources.getString(com.farms.krushisanjivini.R.string.network_info),this)
                finish()
            }

        }

        binding.btnsendOtp.setOnClickListener {
            var networkStatus:Boolean= CheckInternetConnection.checkForInternet(this)
            val mobileNoData:String =binding.mobileno.text.toString()

            if(networkStatus){

                if(checkOtpInput()){
                    if(mobileNoData.length==10){
                        if(testPassword()){
                            sendUserOtp(mobileNoData)
                        }else
                            showAlertDialog("Please enter atleast 6 char for password also make sure password and confirm password should match",this)

                    }else{
                        showAlertDialog("Please enter valid mobile no",this)
                    }
                }
                else
                    showAlertDialog("Please enter mobile no",this)
            }else{
                CheckInternetConnection.showAlertDialog(resources.getString(com.farms.krushisanjivini.R.string.network_info),this)
                //finish()
            }

        }
        binding.otpEditText1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.otpEditText2.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

        binding.otpEditText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.otpEditText3.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })
        binding.otpEditText3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    binding.otpEditText4.requestFocus()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }
        })

    }


    private fun testPassword():Boolean{
        val password:String = binding.registerPassword.text.toString()
        val confirmPwd:String = binding.confirmPassword.text.toString()
        if(password.length==6 && confirmPwd.length==6){
            if(password.equals(confirmPwd)){
                return true
            }else{
                return false
            }
        }else{
            return false
        }

    }

    private fun validCellPhone(number: String?): Boolean {
        return Patterns.PHONE.matcher(number).matches()
    }
    private fun userSignUp() {
        binding.progressbar.visibility = View.VISIBLE
        //var questionText:String? = binding.commentsText.text.toString()
        val username:String?= binding.registerName.text.toString()
        val userEmail:String?="sanjivinikrushi@gmail.com" /*binding.registerEmail.text.toString()*/
        val userPassword:String?= binding.registerPassword.text.toString()
        val mobileNo:String?= binding.mobileno.text.toString()
        val userOtp:String?= ""+binding.otpEditText1.text.toString()+""+
                binding.otpEditText2.text.toString()+""+binding.otpEditText3.text.toString()+
                ""+binding.otpEditText4.text.toString()

        if(userOtp.isNullOrEmpty()){
            showAlertDialog("Please enter correct otp",this)
        }
        else{
            mApiService!!.registerUser(username!!,userEmail!!,mobileNo!!,userPassword!!,userPassword!!,userOtp!!).enqueue(object:
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.code()==200 ||response.code()==201|| response.code()==202){
                        val stringResponse = response.body()?.string()
                        println("$stringResponse")
                        CheckInternetConnection.showRegisteredDialog("User sucessfully resgistered!! Please, try to login!!",this@SignupActivity)
                        SharedPreferencesHelper.invoke(this@SignupActivity).saveUserName(mobileNo)
                        SharedPreferencesHelper.invoke(this@SignupActivity).saveUserPassword( userPassword)
                        SharedPreferencesHelper.invoke(this@SignupActivity).saveUserRegistered(true)

                    }else if(response.code()==401){
                        CheckInternetConnection.showAlertDialog("${response.errorBody()?.string()}",this@SignupActivity)
                    }else{
                        CheckInternetConnection.showAlertDialog("${response.errorBody()?.string()}",this@SignupActivity)
                    }
                    binding.progressbar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println(t.message)
                }
            })
        }




    }


    private fun sendUserOtp(mobileNo:String) {
        binding.progressbar.visibility = View.VISIBLE
        mApiService!!.sendRegOtp(mobileNo!!).enqueue(object:
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.code()==200 ||response.code()==201|| response.code()==202){
                    val stringResponse = response.body()?.string()
                    binding.btnsendOtp.text="Resend OTP"
                    binding.btnsendOtp.visibility=View.GONE
                    binding.btRegistrarse.visibility=View.VISIBLE
                   // binding.enterOtp.visibility=View.VISIBLE
                    binding.registerName.visibility=View.GONE
                    binding.registerPassword.visibility=View.GONE
                    binding.confirmPassword.visibility=View.GONE
                    binding.mobileno.visibility=View.GONE
                    binding.enterOtpLayout.visibility=View.VISIBLE
                    countdowntimer()

                }else if(response.code()==401){
                    CheckInternetConnection.showAlertDialog("${response.errorBody()?.string()}",this@SignupActivity)
                }else{
                    CheckInternetConnection.showAlertDialog("${response.errorBody()?.string()}",this@SignupActivity)
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
                        binding.mobileno.text.isNullOrEmpty()||
                        binding.otpEditText1.text.isNullOrEmpty()||
                        binding.otpEditText2.text.isNullOrEmpty()||
                        binding.otpEditText3.text.isNullOrEmpty()||
                        binding.otpEditText4.text.isNullOrEmpty()
                )
    }

    private fun checkOtpInput(): Boolean {
        return !(
                binding.mobileno.text.isNullOrEmpty()
                )
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun countdowntimer(){
        binding.otptimer.visibility=View.VISIBLE
        binding.otptimerlayout.visibility=View.VISIBLE
        object: CountDownTimer(120000,1000){
            override fun onTick(millisUnitFinished: Long) {
                binding.otptimer.text=""+millisUnitFinished/1000
            }
            override fun onFinish() {
                binding.otptimer.visibility=View.GONE
                binding.otptimerlayout.visibility=View.GONE
                binding.btnsendOtp.text="Resend OTP"
                binding.btnsendOtp.visibility=View.VISIBLE
                binding.btRegistrarse.visibility=View.GONE
              //  binding.enterOtp.visibility=View.GONE

                binding.registerName.visibility=View.VISIBLE
                binding.registerPassword.visibility=View.VISIBLE
                binding.confirmPassword.visibility=View.VISIBLE
                binding.mobileno.visibility=View.VISIBLE
                binding.enterOtpLayout.visibility=View.GONE
            }
        }.start()
    }
}

