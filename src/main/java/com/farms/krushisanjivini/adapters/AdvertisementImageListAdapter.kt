package com.farms.krushisanjivini.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.AdvertisementPostItemBinding
import com.farms.krushisanjivini.model.AddImageUrls
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.utilities.util.getProgressDrawable
import com.farms.krushisanjivini.utilities.util.loadImage


class AdvertisementImageListAdapter(private val imagesList: MutableList<AddImageUrls>)
    : RecyclerView.Adapter<AdvertisementImageListAdapter.ViewHolder>() {
    private var mApiService: ApiServiceInterface?= null
   inner class ViewHolder(val binding: AdvertisementPostItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =AdvertisementPostItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl=imagesList[position].imageUrl
        holder.binding.imgPost.loadImage(imageUrl,
            getProgressDrawable(holder.binding.imgPost.context))


    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}