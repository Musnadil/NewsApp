package com.musnadil.newsapp.ui.search

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.musnadil.newsapp.R
import com.musnadil.newsapp.data.model.ArticleX
import com.musnadil.newsapp.databinding.ItemArticlesBinding
import javax.inject.Inject

class SearchAdapter @Inject() constructor() :
    PagingDataAdapter<ArticleX, SearchAdapter.ViewHolder>(diffCallback) {

//    private lateinit var binding: ItemArticlesBinding
//    private lateinit var context: Context


    //    private val differ = AsyncListDiffer(this, diffCallback)
//    fun submitData(value: List<ArticleX>?) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticlesBinding.inflate(inflater, parent, false)
//        context = parent.context
        return ViewHolder(binding)
    }

//    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

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
                onItemClickListener?.let {
                    it(data)
                }
            }
        }
    }

    private var onItemClickListener: ((ArticleX) -> Unit)? = null
    fun setOnItemClickListener(listener: (ArticleX) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ArticleX>() {
            override fun areItemsTheSame(oldItem: ArticleX, newItem: ArticleX): Boolean {
                return oldItem.content == newItem.content
            }

            override fun areContentsTheSame(oldItem: ArticleX, newItem: ArticleX): Boolean {
                return oldItem.url == newItem.url
            }
        }
    }
}