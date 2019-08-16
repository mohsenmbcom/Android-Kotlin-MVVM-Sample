package com.mohsenmb.googlenewsapisample.ui.headlines

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.mohsenmb.googlenewsapisample.R
import com.mohsenmb.googlenewsapisample.repository.persistence.PersistedArticle
import com.mohsenmb.googlenewsapisample.repository.webservice.Error
import com.mohsenmb.googlenewsapisample.repository.webservice.ErrorType
import com.mohsenmb.googlenewsapisample.ui.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity() {

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	private lateinit var viewModel: HeadlinesViewModel

	private var lastPage = false
	private val adapter = NewsRecyclerAdapter()
	private lateinit var layoutManager: GridLayoutManager

	override fun onStart() {
		super.onStart()
		AndroidInjection.inject(this)
		viewModel = ViewModelProviders.of(this, viewModelFactory)[HeadlinesViewModel::class.java]
		viewModel.getArticlesLiveData().observe(this, Observer(::onHeadlinesChanged))
		viewModel.getErrorsLiveData().observe(this, Observer(::onError))

		view_refresh.isRefreshing = true
		viewModel.firstPage()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		adapter.onItemClickListener = { article ->
			val builder = CustomTabsIntent.Builder()
			val customTabsIntent = builder.build()
			customTabsIntent.launchUrl(this, Uri.parse(article.articleUrl))
		}
		recycler_articles.adapter = adapter

		val spanCount = resources.getInteger(R.integer.news_list_item_span_count)
		layoutManager = GridLayoutManager(this, spanCount)
		layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
			override fun getSpanSize(position: Int): Int {
				return when (position % 7) {
					0 -> resources.getInteger(R.integer.news_list_item_span_count)
					else -> 1
				}
			}
		}
		recycler_articles.layoutManager = layoutManager

		val itemDecoration = SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.news_item_spacing))
		recycler_articles.addItemDecoration(itemDecoration)

		recycler_articles.setOnScrollChangeListener { _, _, _, _, _ ->
			if (!view_refresh.isRefreshing &&
				!lastPage &&
				layoutManager.findLastVisibleItemPosition() >= adapter.itemCount - 3) {
				view_refresh.isRefreshing = true
				viewModel.nextPage()
			}
		}

		view_refresh.setOnRefreshListener {
			lastPage = false
			viewModel.firstPage()
		}
	}

	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(newConfig)
		val spanCount = resources.getInteger(R.integer.news_list_item_span_count)
		layoutManager.spanCount = spanCount
	}

	override fun onStop() {
		viewModel.getArticlesLiveData().removeObservers(this)
		viewModel.getErrorsLiveData().removeObservers(this)
		super.onStop()
	}

	private fun onHeadlinesChanged(list: List<PersistedArticle>?) {
		adapter.updateNews(list ?: listOf())
	}

	private fun onError(error: Error?) {
		view_refresh.isRefreshing = false
		error?.let {
			when (it.type) {
				ErrorType.NoError -> {
				}
				ErrorType.Http -> {
				}
				ErrorType.Timeout -> {
				}
				ErrorType.NetworkOffline -> {
				}
				ErrorType.NoMoreOnlineData -> {
				}
				ErrorType.NoMoreDate -> {
					lastPage = true
				}
				ErrorType.Unknown -> {
				}
			}
			Log.e("ErrorHandling", it.toString())
//			Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
		}
	}
}
