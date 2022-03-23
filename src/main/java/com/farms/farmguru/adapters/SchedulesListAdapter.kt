package com.farms.farmguru.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.farms.farmguru.databinding.ScheduleListingdataItemBinding
import com.farms.farmguru.model.Schedules
import android.R
import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
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
        val flag =schedules[position].statusFlag.toString()
        if(flag=="F"){
            holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        if(flag=="C"){
            holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#00C853"))
        }
        if(flag=="P"){
            holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#9F9C9C"))
        }
        holder.binding.titleValue.text =schedules[position].scheduleDay.toString()
        holder.binding.subTitleValue.text = schedules[position].afterProoningDtDays.toString()
        holder.binding.cropVarietyValue.text = schedules[position].medicineDetails.toString()
        holder.binding.cropSeasonValue.text = schedules[position].activeIngredients.toString()
        holder.binding.diseaseInfectionValue.text = schedules[position].diseaseInfection.toString()
        val date=schedules[position].scheduleDate.toString()
        if(date!=null){
           val  dateFromat =date.split("T")[0]
            holder.binding.scheduleDateValue.text =dateFromat

         /*   var dateInString1 =dateFromat
            var simpleFormat2 =  DateTimeFormatter.ISO_DATE
            var output = LocalDate.parse(dateInString1, simpleFormat2)
            convertTime(output.toString())
            println("output : "+output)
            println(dateFromat)

            val formatter = SimpleDateFormat("dd-MM-yyyy")
            //Parsing the given String to Date object
            //Parsing the given String to Date object
            val date: Date = formatter.parse(dateFromat)
            println("Date object value: $date")*/
        }else{
            holder.binding.scheduleDateValue.text =date
        }

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

    fun convertTime(date: String): String {
        val zonedDateTime = ZonedDateTime.parse(date)
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("pt", "BR"))

        return dtf.format(zonedDateTime)
    }
}