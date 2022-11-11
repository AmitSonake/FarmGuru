package com.farms.krushisanjivini.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.MainActivity
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.databinding.ActivitySplashBinding
import com.farms.krushisanjivini.login.LoginActivity
import java.time.LocalDateTime

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val localtime = LocalDateTime.now().toString()
        println("Current Date and Time = $localtime")
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_slide)
        binding.SplashScreenImage.startAnimation(slideAnimation)
        println("User login session ${SharedPreferencesHelper.invoke(this).getUserLoggedIn()}")
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            //if(!SharedPreferencesHelper.invoke(this).getUserLoggedIn()!!){
            if(SharedPreferencesHelper.invoke(this).getUserLoggedIn() == false){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 4000)
    }
}