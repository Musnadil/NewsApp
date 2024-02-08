package com.musnadil.newsapp.ui.newsarticles

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
import com.musnadil.newsapp.data.model.Article
import com.musnadil.newsapp.databinding.ItemArticlesBinding

class NewsArticlesAdapter(private val onClickItem: OnClickListener) :
    RecyclerView.Adapter<NewsArticlesAdapter.ViewHolder>() {
    private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitData(value: List<Article>?) = differ.submitList(value)

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

        fun bind(data: Article) {
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
        fun onCLickItem(data: Article)
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