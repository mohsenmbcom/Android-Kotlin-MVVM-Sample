package com.mohsenmb.googlenewsapisample.ui.headlines

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mohsenmb.googlenewsapisample.BR
import com.mohsenmb.googlenewsapisample.R
import com.mohsenmb.googlenewsapisample.repository.persistence.PersistedArticle
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * I had written binding adapter before, checkout the following repo on my account
 * 		https://github.com/mohsenmbcom/GithubApiTest
 */
class DataBindingViewHolder(private val binding: ViewDataBinding) :
	RecyclerView.ViewHolder(binding.root) {

	fun bindVariable(variableId: Int, obj: Any) {
		binding.setVariable(variableId, obj)
		binding.executePendingBindings()
	}
}

class NewsRecyclerAdapter() : RecyclerView.Adapter<DataBindingViewHolder>() {

	private val articles = mutableListOf<PersistedArticle>()
	private val disposables = CompositeDisposable()
	var onItemClickListener : (PersistedArticle) -> Unit = { }

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
			DataBindingViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val binding: ViewDataBinding =
			DataBindingUtil.inflate(layoutInflater, R.layout.cell_article, parent, false)
		return DataBindingViewHolder(binding)
	}

	override fun getItemCount(): Int = articles.size

	override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
		holder.bindVariable(BR.article, articles[position])
		holder.bindVariable(BR.onItemClickListener, onItemClickListener)
	}

	fun updateNews(news: List<PersistedArticle>) {
		disposables.clear()
		disposables.add(
			Single.just(articles to news)
				.map { (oldList, newList) ->
					val diff = DiffUtil.calculateDiff(NewsDiffCallback(oldList, newList), false)
					diff to newList
				}
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({ (diff, newList) ->
					articles.apply {
						clear()
						addAll(newList)
					}
					diff.dispatchUpdatesTo(this@NewsRecyclerAdapter)
				}, {
					it.printStackTrace()
				})
		)
	}

}

class NewsDiffCallback(val oldList: List<PersistedArticle>, val newList: List<PersistedArticle>) :
	DiffUtil.Callback() {
	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
		oldList[oldItemPosition].articleUrl == newList[newItemPosition].articleUrl

	override fun getOldListSize(): Int = oldList.size

	override fun getNewListSize(): Int = newList.size

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
		oldList[oldItemPosition].articleUrl == newList[newItemPosition].articleUrl

}

/**
 *  https://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
 */
class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

	override fun getItemOffsets(
		outRect: Rect, view: View,
		parent: RecyclerView, state: RecyclerView.State
	) {
		outRect.left = space
		outRect.right = space
		outRect.bottom = space

		// Add top margin only for the first item to avoid double space between items
		if (parent.getChildLayoutPosition(view) == 0) {
			outRect.top = space
		} else {
			outRect.top = 0
		}
	}
}