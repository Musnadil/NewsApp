package com.musnadil.newsapp.ui.newssources

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.musnadil.newsapp.R
import com.musnadil.newsapp.data.model.Source
import com.musnadil.newsapp.databinding.ItemSourcesBinding
import com.musnadil.newsapp.utility.FetchFaviconTask

class NewsSourcesAdapter(private val onClickItem: OnClickListener) :
    RecyclerView.Adapter<NewsSourcesAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<Source>() {
        override fun areItemsTheSame(oldItem: Source, newItem: Source): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Source, newItem: Source): Boolean {
            return oldItem.url == newItem.url
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitData(value: List<Source>?) = differ.submitList(value)

    inner class ViewHolder(private val binding: ItemSourcesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Source) {
            binding.tvSource.text = data.name
            val websiteUrl = data.url
            val fetchFaviconTask = FetchFaviconTask { faviconUrl ->
                if (faviconUrl.isNotEmpty()) {
                    Glide.with(binding.root)
                        .load(faviconUrl)
                        .error(R.drawable.ic_newspaper_24)
                        .into(binding.ivIcon)
                } else {
                    Glide.with(binding.root)
                        .load(R.drawable.ic_newspaper_24)
                        .error(R.drawable.ic_newspaper_24)
                        .into(binding.ivIcon)
                }
            }
            fetchFaviconTask.execute(websiteUrl)

            binding.root.setOnClickListener {
                onClickItem.onCLickItem(data)
            }
        }
    }

    interface OnClickListener {
        fun onCLickItem(data: Source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemSourcesBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }
}