package com.farms.krushisanjivini.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.databinding.QuestionListingItemBinding
import com.farms.krushisanjivini.model.Questions


class QuestionsListAdapter(private val questions: MutableList<Questions>)
    : RecyclerView.Adapter<QuestionsListAdapter.ViewHolder>() {

   inner class ViewHolder(val binding: QuestionListingItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =QuestionListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("${questions[position].queText.toString()}")

        if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("kn")){
            holder.binding.titleLabel.text="ಪ್ರಶ್ನೆಯ ವಿಷಯ:"
            holder.binding.cropSeasonLabel.text="ವಿನಂತಿಯ ಸ್ಥಿತಿ:"
            holder.binding.answerLabel.text="ನಿಮ್ಮ ಉತ್ತರ:"
        }
        holder.binding.titleValue.text =questions[position].queText.toString()
        holder.binding.subTitleValue.text = questions[position].queDateTime.toString()
        holder.binding.cropVarietyValue.text = questions[position].assignedTo.toString()
        holder.binding.cropSeasonValue.text = questions[position].status.toString()
        if(questions[position].answer.toString().equals("null")){
            holder.binding.answerValue.visibility = View.GONE
            holder.binding.answerLabel.visibility = View.GONE
        }else{
            holder.binding.answerValue.visibility = View.VISIBLE
            holder.binding.answerLabel.visibility = View.VISIBLE
        }
        holder.binding.answerValue.text =questions[position].answer.toString()

        // if()

    }

    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return questions.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}