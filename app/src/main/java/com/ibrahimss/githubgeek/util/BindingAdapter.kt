package com.ibrahimss.githubgeek.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ibrahimss.githubgeek.R
import com.ibrahimss.githubgeek.adapter.UserAdapter
import com.ibrahimss.githubgeek.data.model.UserResponse

@BindingAdapter("userList")
fun RecyclerView.setUserList(users: List<UserResponse>?) {
    if (users != null) {
        this.visibility = View.VISIBLE
        (adapter as UserAdapter).submitList(users)
    }
}

@BindingAdapter("loadImgFromUrl")
fun ImageView.loadImageFromUrl(url: String?) {
    Glide.with(this)
        .clear(this)
    val requestOption = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .centerCrop()

    url?.apply {
        Glide.with(this@loadImageFromUrl.context)
            .load(url)
            .apply(requestOption)
            .apply(RequestOptions.circleCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this@loadImageFromUrl)
    }
}

/**
 * Adapter to load text if it is not null. Otherwise, hide the view.
 */
@BindingAdapter("loadNullableText")
fun TextView.loadNullableText(text: String?) {
    if (text != null) {
        this.visibility = View.VISIBLE
        this.text = text
    } else {
        this.visibility = View.GONE
    }
}

/**
 * Adapter to check if it's data not null. Otherwise, hide the view. For Text data only.
 */
@BindingAdapter("visibilityOnDataNullable")
fun View.visibilityOnDataNullable(text: Any?) {
    if (text != null && text != "") {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

/**
 * Adapter to check if user's name is null assign username instead.
 */
@BindingAdapter("name", "username", requireAll = true)
fun TextView.loadNameOtherwiseUsername(name: String?, username: String?) {
    if (name != null && name != "") {
        this.text = name
    } else {
        this.text = username
    }
}

@BindingAdapter("srcOnState")
fun FloatingActionButton.setIconSourceBasedOnState(state: Boolean) {
    if (state) {
        this.setImageResource(R.drawable.baseline_favorite_24)
    } else {
        this.setImageResource(R.drawable.baseline_favorite_border_24)
    }
}