package com.zackone.readnews.favorite.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zackone.core.databaseModule
import com.zackone.core.domain.model.Article
import com.zackone.core.networkModule
import com.zackone.core.repositoryModule
import com.zackone.readnews.ui.adapter.SearchRecycleViewAdapter
import com.zackone.readnews.favorite.R
import com.zackone.readnews.favorite.databinding.FragmentFavoriteBinding
import com.zackone.readnews.favorite.viewModelModule
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment: Fragment(R.layout.fragment_favorite) {
    private val loadFeatures by lazy { loadKoinModules(listOf(
        databaseModule, networkModule, repositoryModule, viewModelModule
    )) }

    private fun injectFeatures() = loadFeatures

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        setupObserver()
        setupSearchBar()

        return binding.root
    }

    private fun setupObserver() {
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            showArticles(articles)
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { articles ->
            showSearchResults(articles)
        }

        viewModel.fetchFavoriteArticles()
    }

    private fun setupSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                viewModel.searchFavoriteArticles(searchView.text.toString())
                true
            }

            searchView.editText.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.queryChannel.value = text.toString()
                }

                override fun afterTextChanged(p0: Editable?) {}

            })
        }
    }

    private fun showArticles(articles: List<Article>) {
        val adapter = FavoriteArticleRecycleViewAdapter(articles)
        binding.rvArticle.adapter = adapter
        binding.rvArticle.layoutManager = LinearLayoutManager(requireActivity())
        binding.tvFavoriteEmpty.visibility = if(articles.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showSearchResults(articles: List<Article>?) {
        if (articles.isNullOrEmpty()) return

        val adapter = SearchRecycleViewAdapter(articles) { article ->
            binding.searchBar.setText(article.title)
            binding.searchView.editText.setText(article.title)
            binding.searchView.clearFocus()
            binding.searchView.editText.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }

        binding.rvSearch.adapter = adapter
        binding.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
    }
}