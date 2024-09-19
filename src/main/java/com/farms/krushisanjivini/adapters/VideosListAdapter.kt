package com.farms.krushisanjivini.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.farms.krushisanjivini.R
import com.farms.krushisanjivini.databinding.VideosRowItemBinding
import com.farms.krushisanjivini.model.AddYoutubeUrls
import com.farms.krushisanjivini.network.ApiServiceInterface
import com.farms.krushisanjivini.ui.videos.VideoPreviewActivity


class VideosListAdapter(
    private val videosList: MutableList<AddYoutubeUrls>,
    private var userLanguage: String?,
    private var userLanguageID: Int
)
    : RecyclerView.Adapter<VideosListAdapter.ViewHolder>() {
    private var mApiService: ApiServiceInterface?= null
   inner class ViewHolder(val binding: VideosRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =VideosRowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.videoTitle.text =videosList[position].Title
        holder.binding.videoDescription.text = videosList[position].Description

        val options = RequestOptions()
            .placeholder(R.drawable.ks)
            .error(R.drawable.ks)
        Glide.with(holder.binding.root)
            .setDefaultRequestOptions(options)
            .load(videosList[position].ThumbnailUrl)
            .into(holder.binding.logoVideoview)

        holder.binding.videoUrlLayout.setOnClickListener {
            val intent = Intent(it.context, VideoPreviewActivity::class.java)
            intent.putExtra("videoUrl", videosList[position].VideoUrlText)
            ContextCompat.startActivity(it.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return videosList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}