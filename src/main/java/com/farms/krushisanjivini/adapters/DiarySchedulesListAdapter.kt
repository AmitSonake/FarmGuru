package com.farms.krushisanjivini.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.util.SharedPreferencesHelper
import com.farms.krushisanjivini.databinding.DiaryScheduleListingItemBinding
import com.farms.krushisanjivini.model.Schedules
import java.text.SimpleDateFormat


class DiarySchedulesListAdapter(private val schedules: MutableList<Schedules>)
    : RecyclerView.Adapter<DiarySchedulesListAdapter.ViewHolder>() {

   inner class ViewHolder(val binding: DiaryScheduleListingItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =DiaryScheduleListingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flag = schedules[position].statusFlag
        //if(flag=="F"){
            holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
           // holder.binding.cropVarietyValue.setBackgroundColor(Color.parseColor("#FFFFFF"))
        //}
       // if(flag=="C"){
       //     holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#00C853"))
        //    holder.binding.cropVarietyValue.setBackgroundColor(Color.parseColor("#00C853"))
        //}
       // if(flag=="P"){
        //    holder.binding.relativeLayout.setBackgroundColor(Color.parseColor("#9F9C9C"))
        //    holder.binding.cropVarietyValue.setBackgroundColor(Color.parseColor("#9F9C9C"))
        //}
        if(schedules[position].sprayScheduleNote.toString().equals("null")||schedules[position].sprayScheduleNote.toString().isNullOrEmpty()){
            holder.binding.sprayScheduleLabel.visibility= View.GONE
            holder.binding.sprayScheduleNoteValue.visibility= View.GONE
        }else{
            holder.binding.sprayScheduleLabel.visibility= View.VISIBLE
            holder.binding.sprayScheduleNoteValue.visibility= View.VISIBLE
        }
        if(schedules[position].sprayMedicineBrand.toString().equals("null")||schedules[position].sprayMedicineBrand.toString().isNullOrEmpty()){
            holder.binding.sprayMedicineBrandLabel.visibility= View.GONE
            holder.binding.sprayMedicineBrandLabelValue.visibility= View.GONE
        }else{
            holder.binding.sprayMedicineBrandLabel.visibility= View.VISIBLE
            holder.binding.sprayMedicineBrandLabelValue.visibility= View.VISIBLE
        }

        if(schedules[position].sprayIngredients.toString().equals("null")||schedules[position].sprayIngredients.toString().isNullOrEmpty()){
            holder.binding.sprayIngredientsLabel.visibility= View.GONE
            holder.binding.sprayIngredientsLabelValue.visibility= View.GONE
        }else{
            holder.binding.sprayIngredientsLabel.visibility= View.VISIBLE
            holder.binding.sprayIngredientsLabelValue.visibility= View.VISIBLE
        }

        if(schedules[position].sprayPurpose.toString().equals("null")||schedules[position].sprayPurpose.toString().isNullOrEmpty()){
            holder.binding.sprayPurposeLabel.visibility= View.GONE
            holder.binding.sprayPurposeLabelValue.visibility= View.GONE
        }else{
            holder.binding.sprayPurposeLabel.visibility= View.VISIBLE
            holder.binding.sprayPurposeLabelValue.visibility= View.VISIBLE
        }

        if(schedules[position].basalDoseNote.toString().equals("null")||schedules[position].basalDoseNote.toString().isNullOrEmpty()){
            holder.binding.basalDoseNoteLabel.visibility= View.GONE
            holder.binding.basalDoseNoteLabelValue.visibility= View.GONE
        }else{
            holder.binding.basalDoseNoteLabel.visibility= View.VISIBLE
            holder.binding.basalDoseNoteLabelValue.visibility= View.VISIBLE
        }

        if(schedules[position].basalDoseMedicineBrand.toString().equals("null")||schedules[position].basalDoseMedicineBrand.toString().isNullOrEmpty()){
            holder.binding.basalDoseMedicineBrandLabel.visibility= View.GONE
            holder.binding.basalDoseMedicineBrandLabelValue.visibility= View.GONE
        }else{
            holder.binding.basalDoseMedicineBrandLabel.visibility= View.VISIBLE
            holder.binding.basalDoseMedicineBrandLabelValue.visibility= View.VISIBLE
        }

        if(schedules[position].basalDoseIngredients.toString().equals("null")||schedules[position].basalDoseIngredients.toString().isNullOrEmpty()){
            holder.binding.basalDoseIngredientsLabel.visibility= View.GONE
            holder.binding.basalDoseIngredientsLabelValue.visibility= View.GONE
        }else{
            holder.binding.basalDoseIngredientsLabel.visibility= View.VISIBLE
            holder.binding.basalDoseIngredientsLabelValue.visibility= View.VISIBLE
        }
        if(schedules[position].basalDosePurpose.toString().equals("null")||schedules[position].basalDosePurpose.toString().isNullOrEmpty()){
            holder.binding.basalDosePurposeLabel.visibility= View.GONE
            holder.binding.basalDosePurposeLabelValue.visibility= View.GONE
        }else{
            holder.binding.basalDosePurposeLabel.visibility= View.VISIBLE
            holder.binding.basalDosePurposeLabelValue.visibility= View.VISIBLE
        }

        if(schedules[position].noteIfAny.toString().equals("null")||schedules[position].noteIfAny.toString().isNullOrEmpty()){
            holder.binding.noteIfAnyLabel.visibility= View.GONE
            holder.binding.noteIfAnyLabelValue.visibility= View.GONE
        }else{
            holder.binding.noteIfAnyLabel.visibility= View.VISIBLE
            holder.binding.noteIfAnyLabelValue.visibility= View.VISIBLE
        }

        if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("kn")){
            holder.binding.titleLabel.text="ವೇಳಾಪಟ್ಟಿ ದಿನ"
            // holder.binding.subTitleLabel.text="ಮೆಡಿಸಿನ್ ಬ್ರಾಂಡ್"
            holder.binding.sprayScheduleLabel.text="ಸ್ಪ್ರೇ ವೇಳಾಪಟ್ಟಿ"
            holder.binding.sprayMedicineBrandLabel.text="ಮೆಡಿಸಿನ್ ಬ್ರಾಂಡ್"
            holder.binding.sprayIngredientsLabel.text="ಪದಾರ್ಥಗಳು"
            holder.binding.sprayPurposeLabel.text="ಉದ್ದೇಶ"
            holder.binding.basalDoseNoteLabel.text="ತಳದ ಟಿಪ್ಪಣಿ"
            holder.binding.basalDoseMedicineBrandLabel.text="ಗೊಬ್ಬರಗಳ ಹೆಸರು"
            holder.binding.basalDoseIngredientsLabel.text="ಸಕ್ರಿಯ ಘಟಕ"
            holder.binding.basalDosePurposeLabel.text="ಮೂಲ ಉದ್ದೇಶ"
            holder.binding.noteIfAnyLabel.text="ಟಿಪ್ಪಣಿಗಳು"
            holder.binding.scheduleDateLabel.text="ವೇಳಾಪಟ್ಟಿ ದಿನಾಂಕ"
        }
        if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("EN")){
            holder.binding.titleLabel.text ="Schedule Day:"
        }else{
            holder.binding.titleLabel.text ="ಪವೇಳಾಪಟ್ಟಿ ದಿನ:"
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
        holder.binding.sprayScheduleNoteValue.text = schedules[position].sprayScheduleNote.toString()
        holder.binding.sprayMedicineBrandLabelValue.text = schedules[position].sprayMedicineBrand.toString()
        holder.binding.sprayIngredientsLabelValue.text = schedules[position].sprayIngredients.toString()
        holder.binding.sprayPurposeLabelValue.text = schedules[position].sprayPurpose.toString()
        holder.binding.basalDoseNoteLabelValue.text=schedules[position].basalDoseNote.toString()
        holder.binding.basalDoseMedicineBrandLabelValue.text = schedules[position].basalDoseMedicineBrand.toString()
        holder.binding.basalDoseIngredientsLabelValue.text = schedules[position].basalDoseIngredients.toString()
        holder.binding.basalDosePurposeLabelValue.text = schedules[position].basalDosePurpose.toString()
        holder.binding.noteIfAnyLabelValue.text = schedules[position].noteIfAny.toString()

       // holder.binding.cropVarietyValue.loadDataWithBaseURL(null,schedules[position].scheduleText,"text/html", "utf-8", null)
        /*holder.binding.cropSeasonValue.text = schedules[position].activeIngredients.toString()
        holder.binding.diseaseInfectionValue.text = schedules[position].diseaseInfection.toString()*/
        val date= schedules[position].scheduleDate
        val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        val formattedDate = formatter.format(parser.parse(date))
        val  dateFormat =date.split("T")[0]
        if(SharedPreferencesHelper.invoke(holder.binding.root.context).getSelectedLanguage().equals("EN")){
            holder.binding.scheduleDateLabel.text ="Schedule Date:"
        }else{
            holder.binding.scheduleDateLabel.text ="ವೇಳಾಪಟ್ಟಿ ದಿನಾಂಕ:"
        }
        holder.binding.scheduleDateValue.text =formattedDate.toString()

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

}