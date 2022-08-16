package com.farms.farmguru.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farms.farmguru.databinding.QuestionListingItemBinding
import com.farms.farmguru.model.Questions


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
        holder.binding.titleValue.text =questions[position].queText.toString()
        holder.binding.subTitleValue.text = questions[position].queDateTime.toString()
        holder.binding.cropVarietyValue.text = questions[position].assignedTo.toString()
        holder.binding.cropSeasonValue.text = questions[position].status.toString()

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