package com.farms.farmguru.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.farms.farmguru.databinding.ImageHolderBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class HomeSliderImageAdapter(context: Context) :
    SliderViewAdapter<HomeSliderImageAdapter.VH>() {
    private var mSliderItems = ArrayList<Int>()
    var context: Context= context
    fun renewItems(sliderItems: ArrayList<Int>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: Int) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH {
        val binding = ImageHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VH(binding)
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        //load image into view
        Glide.with(context)
            .load(mSliderItems[position])
            .into(viewHolder.binding.imageSlider)
       // Picasso.get().load(mSliderItems[position]).fit().into(viewHolder.binding.imageSlider)
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    inner class VH(val binding:ImageHolderBinding) : ViewHolder(binding.root)
}