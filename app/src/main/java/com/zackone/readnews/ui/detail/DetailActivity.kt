package com.zackone.readnews.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zackone.core.domain.model.Article
import com.zackone.readnews.R
import com.zackone.readnews.databinding.ActivityDetailBinding
import com.zackone.readnews.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModel()

    private var article: Article? = null
    private var menu: Int = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Article"

        article = intent.getParcelableExtra("article")
        menu = intent.getIntExtra("menu", R.id.navigation_home)

        showDetailArticle()
        setupFabButton(article?.isFavorite ?: false)
    }

    private fun showDetailArticle() {
        article?.let {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("MMMM dd, yyyy, 'at' h:mm a", Locale.getDefault())

            val publishedAt = inputDateFormat.parse(it.publishedAt)
            val parsedPublishedAt = outputDateFormat.format(publishedAt!!)

            val url = it.url

            binding.tvTitle.text = it.title
            binding.tvAuthor.text = it.author
            binding.tvPublishedAt.text = parsedPublishedAt

            if (!it.description.isNullOrEmpty()) {
                binding.detailTvDetail.text = Html.fromHtml(it.description, Html.FROM_HTML_MODE_COMPACT)
                binding.detailTvDetail.movementMethod = LinkMovementMethod.getInstance()
            }

            if (!it.urlToImage.isNullOrEmpty()) {
                Glide.with(this)
                    .load(it.urlToImage)
                    .into(binding.detailIvLogo)
            }

            binding.detailBtnLink.setOnClickListener {
                openLinkInBrowser(url)
            }
        }
    }

    private fun setupFabButton(isFavorite: Boolean) {
        if (isFavorite) setupFabDeleteFavorite(article!!)
        else setupFabInsertFavorite(article!!)
    }

    private fun setupFabInsertFavorite(article: Article) {
        binding.detailFabFavorite.setImageResource(R.drawable.ic_favorite_inactive)
        binding.detailFabFavorite.setOnClickListener {
            viewModel.setFavorite(article, true)
            setupFabButton(true)
        }
    }

    private fun setupFabDeleteFavorite(article: Article) {
        binding.detailFabFavorite.setImageResource(R.drawable.ic_favorite_active)
        binding.detailFabFavorite.setOnClickListener {
            viewModel.setFavorite(article, false)
            setupFabButton(false)
        }
    }

    private fun openLinkInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("menu", menu)
        }
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}