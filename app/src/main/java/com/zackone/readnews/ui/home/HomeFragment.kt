package com.zackone.readnews.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zackone.core.domain.model.Article
import com.zackone.readnews.R
import com.zackone.readnews.databinding.FragmentHomeBinding
import com.zackone.readnews.ui.adapter.ArticleRecycleViewAdapter
import com.zackone.readnews.ui.adapter.SearchRecycleViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupObserver()
        setupSearchBar()

        return binding.root
    }

    private fun setupObserver() {
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            showArticles(articles)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.clLoader.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { articles ->
            showSearchResults(articles)
        }

        viewModel.fetchArticles()
    }

    private fun setupSearchBar() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                viewModel.searchArticles(searchView.text.toString())
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

    private fun showArticles(articles: List<Article>?) {
        if (articles.isNullOrEmpty()) return

        val adapter = ArticleRecycleViewAdapter(articles)
        binding.rvArticle.adapter = adapter
        binding.rvArticle.layoutManager = LinearLayoutManager(requireActivity())
        binding.tvArticleEmpty.visibility = if(articles.isEmpty()) View.VISIBLE else View.GONE
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