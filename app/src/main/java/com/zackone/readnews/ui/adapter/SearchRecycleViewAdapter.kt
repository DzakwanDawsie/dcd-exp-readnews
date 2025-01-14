package com.zackone.readnews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zackone.core.domain.model.Article
import com.zackone.readnews.databinding.ItemSearchBinding

class SearchRecycleViewAdapter(
    private val articles: List<Article>,
    private val searchCallback: ((Article) -> Unit)
):
    RecyclerView.Adapter<SearchRecycleViewAdapter.SearchViewHolder>() {

    class SearchViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, searchCallback: ((Article) -> Unit)) {
            binding.itemTvTitle.text = article.title
            binding.root.setOnClickListener {
                searchCallback.invoke(article)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(articles[position], searchCallback)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}