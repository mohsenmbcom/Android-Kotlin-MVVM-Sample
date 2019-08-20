package com.mohsenmb.googlenewsapisample.ui.headlines

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.mohsenmb.googlenewsapisample.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

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
					.into(imageView, object : Callback {
						override fun onSuccess() {
							val subscriber = Single.just(imageView)
								.map { img ->
									val (fromZ, toZ) = Random.nextInt(40).run {
										return@run when {
											this < 20 -> 0.0F to (this + 10) / 100F
											else -> (this + 10) / 100F to 0.0F
										}
									}
									val zoom = ScaleAnimation(
										1F + fromZ,
										1F + toZ,
										1F + fromZ,
										1F + toZ,
										img.width * Random.nextFloat(),
										img.height * Random.nextFloat()
									).apply {
										repeatCount = Animation.INFINITE
										repeatMode = Animation.REVERSE
										duration = TimeUnit.SECONDS.toMillis(15)
										fillBefore = true
										fillAfter = true
										startOffset = Random.nextLong(1000)
										interpolator = AccelerateDecelerateInterpolator()
									}
									img to zoom
								}
								.subscribeOn(Schedulers.single())
								.observeOn(AndroidSchedulers.mainThread())
								.subscribe { (img, anim), _ ->
									img.startAnimation(anim)
								}
						}

						override fun onError(e: Exception?) {
							e?.printStackTrace()
						}
					})
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