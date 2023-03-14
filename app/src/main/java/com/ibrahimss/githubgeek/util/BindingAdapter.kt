package com.ibrahimss.githubgeek.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ibrahimss.githubgeek.adapter.UserAdapter
import com.ibrahimss.githubgeek.model.UserResponse

@BindingAdapter("userList")
fun RecyclerView.setUserList(users: List<UserResponse>?) {
    if (users != null) {
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