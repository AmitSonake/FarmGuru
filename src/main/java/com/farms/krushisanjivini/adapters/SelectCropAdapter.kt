package com.farms.krushisanjivini.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.SelectCropItemBinding
import com.farms.krushisanjivini.model.CropResponse
import com.farms.krushisanjivini.plotregistration.CropPlotRegistrationActivity


class SelectCropAdapter(private val crops: MutableList<CropResponse>)
    : RecyclerView.Adapter<SelectCropAdapter.ViewHolder>() {

   inner class ViewHolder(val binding: SelectCropItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =SelectCropItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
       /* val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_view,parent,false)

        // return the view holder
        return ViewHolder(view)*/

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
       holder.binding.tvAnimal.text = crops[position].CropName
        holder.binding.cardView.setOnClickListener {
            val intent = Intent(it.context,CropPlotRegistrationActivity::class.java)
            intent.putExtra("cropId",crops[position].CropId)
            intent.putExtra("cropName",crops[position].CropName)
            startActivity(it.context,intent,null)
        }

    }


    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return crops.size
    }


   // inner class ViewHolder(val binding: CustomViewBinding) : RecyclerView.ViewHolder(binding.root)


    // this two methods useful for avoiding duplicate item
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}