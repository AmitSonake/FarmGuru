package com.farms.farmguru.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.farms.farmguru.ComplaintRegistrationActivity
import com.farms.farmguru.databinding.CustomViewBinding
import com.farms.farmguru.ui.myplots.MyPlotActivity
import com.farms.farmguru.ui.weather.WeatherActivity


class RecyclerViewAdapter(private val animals: MutableList<String>)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

   inner class ViewHolder(val binding: CustomViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflate the custom view from xml layout file
        val binding =CustomViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
       /* val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_view,parent,false)

        // return the view holder
        return ViewHolder(view)*/

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display the current animal
       holder.binding.tvAnimal.text = animals[position]
        holder.binding.tvAnimal.setOnClickListener {
            if(position==0){
                var intent = Intent(it.context,WeatherActivity::class.java)
                startActivity(it.context,intent,null)
            }
           else  if(position==2){
                var intent = Intent(it.context,MyPlotActivity::class.java)
                startActivity(it.context,intent,null)
            }
           else  if(position==4){
                var intent = Intent(it.context,ComplaintRegistrationActivity::class.java)
                startActivity(it.context,intent,null)
            }
        }
    }


    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return animals.size
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