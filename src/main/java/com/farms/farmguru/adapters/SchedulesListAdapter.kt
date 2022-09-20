package com.farms.farmguru.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.farmguru.databinding.ScheduleListingdataItemBinding
import com.farms.farmguru.model.Schedules
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class SchedulesListAdapter(private val schedules: MutableList<Schedules>)
    : RecyclerView.Adapter<SchedulesListAdapter.ViewHolder>() {

   inner class ViewHolder(val binding: ScheduleListingdataItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =ScheduleListingdataItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flag = schedules[position].statusFlag
        if(flag=="F"){
            holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
            holder.binding.cropVarietyValue.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        if(flag=="C"){
            holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#00C853"))
            holder.binding.cropVarietyValue.setBackgroundColor(Color.parseColor("#00C853"))
        }
        if(flag=="P"){
            holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#9F9C9C"))
            holder.binding.cropVarietyValue.setBackgroundColor(Color.parseColor("#9F9C9C"))
        }
        holder.binding.titleValue.text =schedules[position].scheduleDay.toString()
        if(schedules[position].afterProoningDtDays>0){
            if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("EN")){
                holder.binding.subTitleLabel.text ="After Prooning Days:"
            }else{
                holder.binding.subTitleLabel.text ="ಪ್ರೂನಿಂಗ್ ದಿನಗಳ ನಂತರ:"
            }

        }else if(schedules[position].afterProoningDtDays<0){
            if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("EN")){
                holder.binding.subTitleLabel.text ="Before Prooning Days:"
            }else{
                holder.binding.subTitleLabel.text ="ಪ್ರೂನಿಂಗ್ ದಿನಗಳ ಮೊದಲು:"
            }

        }else{
            if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("EN")){
                holder.binding.subTitleLabel.text ="On Prooning Day:"
            }else{
                holder.binding.subTitleLabel.text ="ಪ್ರೂನಿಂಗ್ ದಿನದಂದು:"
            }

        }
        holder.binding.subTitleValue.text = schedules[position].afterProoningDtDays.toString()
        holder.binding.cropVarietyValue.loadDataWithBaseURL(null,schedules[position].scheduleText,"text/html", "utf-8", null)
        /*holder.binding.cropSeasonValue.text = schedules[position].activeIngredients.toString()
        holder.binding.diseaseInfectionValue.text = schedules[position].diseaseInfection.toString()*/
        val date= schedules[position].scheduleDate
        val  dateFormat =date.split("T")[0]
        holder.binding.scheduleDateValue.text =dateFormat

    }

    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return schedules.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTime(date: String): String {
        val zonedDateTime = ZonedDateTime.parse(date)
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))

        return dtf.format(zonedDateTime)
    }
}