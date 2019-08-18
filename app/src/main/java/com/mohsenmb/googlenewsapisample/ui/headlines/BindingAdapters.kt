package com.mohsenmb.googlenewsapisample.ui.headlines

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.mohsenmb.googlenewsapisample.R
import com.squareup.picasso.Picasso
import java.util.*

object BindingAdapters {

	@JvmStatic
	@BindingAdapter("imageUrl")
	fun setImageUrl(imageView: AppCompatImageView, url: String?) {
		if (url == null) {
			imageView.setImageResource(R.drawable.img_news_placeholder)
		} else {
			imageView.post {
				Picasso.get()
					.load(url)
					.placeholder(R.drawable.img_news_placeholder)
					.error(R.drawable.img_news_placeholder)
					.fit()
					.centerCrop()
					.into(imageView)
			}
		}
	}

	@JvmStatic
	@BindingAdapter("time")
	fun setImageUrl(textView: AppCompatTextView, date: Date?) {
		if (date != null) {
			textView.text = TimeAgo.using(date.time)
		}
	}
}