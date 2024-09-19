package com.farms.krushisanjivini.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.recyclerview.widget.RecyclerView
import com.farms.krushisanjivini.databinding.VideoViewBinding
import com.farms.krushisanjivini.model.AddVideoUrl

class VideoAdapter internal constructor(private val youtubeVideoList: List<AddVideoUrl>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: VideoViewBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webChromeClient = object : WebChromeClient() {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VideoViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
        /*val view = LayoutInflater.from(parent.context).inflate(R.layout.video_view, parent, false)
        return VideoViewHolder(view)*/
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       /* holder.binding.webView.settings.javaScriptEnabled = true
        holder.binding.webView.webChromeClient = object : WebChromeClient()*/
        holder.binding.webView.loadData(youtubeVideoList[position].videoUrl!!, "text/html", "utf-8")
    }

    override fun getItemCount(): Int {
        // number of items in the data set held by the adapter
        return youtubeVideoList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}