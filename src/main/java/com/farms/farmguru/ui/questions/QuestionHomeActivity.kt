package com.farms.farmguru.ui.questions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.farms.farmguru.ComplaintRegistrationActivity
import com.farms.farmguru.databinding.ActivityQuestionHomeBinding

class QuestionHomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityQuestionHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQuestionHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonQuestion.setOnClickListener {
            val intent = Intent(this,ComplaintRegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.buttonViewStatus.setOnClickListener {
            val intent = Intent(this,MyQuestionListingActivity::class.java)
            startActivity(intent)
        }
    }
}