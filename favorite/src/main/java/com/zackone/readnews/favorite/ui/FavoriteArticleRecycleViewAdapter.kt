package com.zackone.readnews.favorite.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zackone.core.domain.model.Article
import com.zackone.readnews.databinding.ItemArticleDetailBinding
import com.zackone.readnews.R
import com.zackone.readnews.ui.detail.DetailActivity

class FavoriteArticleRecycleViewAdapter(private val articles: List<Article>):
    RecyclerView.Adapter<FavoriteArticleRecycleViewAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(private val binding: ItemArticleDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.itemTvTitle.text = article.title
            binding.itemTvDescription.text = article.description

            if (!article.urlToImage.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(article.urlToImage)
                    .into(binding.itemIvCover)
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
                        putExtra("article", article)
                        putExtra("menu", R.id.navigation_favorite)
                    }
                    binding.root.context.startActivity(intent)
                    (binding.root.context as Activity).finish()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {
        val binding = ItemArticleDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}