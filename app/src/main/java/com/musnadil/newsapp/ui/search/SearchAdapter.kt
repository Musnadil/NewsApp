package com.musnadil.newsapp.ui.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.musnadil.newsapp.R
import com.musnadil.newsapp.data.model.ArticleX
import com.musnadil.newsapp.databinding.ItemArticlesBinding

class SearchAdapter(private val onClickItem: OnClickListener) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<ArticleX>() {
        override fun areItemsTheSame(oldItem: ArticleX, newItem: ArticleX): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: ArticleX, newItem: ArticleX): Boolean {
            return oldItem.url == newItem.url
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitData(value: List<ArticleX>?) = differ.submitList(value)

    inner class ViewHolder(private val binding: ItemArticlesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.parseColor("#f3f3f3"))
            .setBaseAlpha(1F)
            .setHighlightColor(Color.parseColor("#CFCFCF"))
            .setHighlightAlpha(1F)
            .setDropoff(50F)
            .build()

        private val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        fun bind(data: ArticleX) {
            Glide.with(binding.root)
                .load(data.urlToImage)
                .placeholder(shimmerDrawable)
                .error(R.drawable.default_image)
                .into(binding.ivNews)
            binding.tvArticleTitle.text = data.title
            binding.root.setOnClickListener {
                onClickItem.onCLickItem(data)
            }
        }
    }

    interface OnClickListener {
        fun onCLickItem(data: ArticleX)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemArticlesBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = differ.currentList[position]
        data.let {
            holder.bind(data)
        }
    }
}