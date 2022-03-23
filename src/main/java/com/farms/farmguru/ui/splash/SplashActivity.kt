package com.farms.farmguru.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.MainActivity
import com.farms.farmguru.R
import com.farms.farmguru.databinding.ActivitySplashBinding
import com.farms.farmguru.login.LoginActivity
import com.farms.farmguru.ui.mydiary.DiaryNoteActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_slide)
        binding.SplashScreenImage.startAnimation(slideAnimation)

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            if(SharedPreferencesHelper.invoke(this).getUserLoggedIn()==false){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }

            finish()
        }, 3000)
    }
}