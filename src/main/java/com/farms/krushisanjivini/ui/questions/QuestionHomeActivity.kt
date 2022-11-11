package com.farms.krushisanjivini.ui.questions

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.databinding.ActivityQuestionHomeBinding

class QuestionHomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityQuestionHomeBinding

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
        binding= ActivityQuestionHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(SharedPreferencesHelper.invoke(this).getSelectedLanguage().equals("kn")){
            binding.buttonQuestion.text="ಪ್ರಶ್ನೆಯನ್ನು ಕೆಳಿರಿ"
            binding.buttonViewStatus.text="ಉತ್ತರವನ್ನು ನೋಡಿರಿ"
            binding.questionLabel.text="ನಿಮ್ಮ ತೋಟದಲ್ಲಿ ಎನಾದರು ಸಮಸ್ಯೆ ಕಂಡು ಬಂದಲ್ಲಿ ಪ್ರಶ್ನೆಯನ್ನು ಕೆಳಿರಿ ಮೇಲೆ ಕ್ಲಿಕ ಮಾಡಿ ನಿಮ್ಮ ಪ್ರಶ್ನೆಯನ್ನು ಕೆಳಬಹುದು."
            binding.viewLabel.text="ನಿಮ್ಮ ಪ್ರಶ್ನೆಯ ಸ್ಥಿತಿಯನ್ನು ಟ್ರ್ಯಾಕ್ ಮಾಡಲು \"ಉತ್ತರವನ್ನು ನೊಡಿರಿ\" ಮೆಲೆ ಕ್ಲಿಕ್ ಮಾಡಿ ಸ್ಥಿತಿಯನ್ನು ವೀಕ್ಷಿಸಿ."
        }else{
            binding.buttonQuestion.text="Ask Question"
            binding.buttonViewStatus.text="View Status"
            binding.questionLabel.text="You found any problem or diseage information for your farm you can click on Ask Question button to register problem"
            binding.viewLabel.text="For tracking status of your question, please click on View Status button"
        }

        binding.buttonQuestion.setOnClickListener {
            val intent = Intent(this, com.farms.krushisanjivini.ComplaintRegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.buttonViewStatus.setOnClickListener {
            val intent = Intent(this,MyQuestionListingActivity::class.java)
            startActivity(intent)
        }
    }
}