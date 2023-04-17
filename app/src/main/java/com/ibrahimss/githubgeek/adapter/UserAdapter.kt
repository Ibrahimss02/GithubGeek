package com.ibrahimss.githubgeek.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ibrahimss.githubgeek.databinding.ItemUserBinding
import com.ibrahimss.githubgeek.data.model.UserResponse
import com.ibrahimss.githubgeek.util.UserDiffUtilCallback

class UserAdapter(private val clickListener: OnUserItemClickListener): ListAdapter<UserResponse, UserAdapter.UserViewHolder>(UserDiffUtilCallback()) {

    inner class UserViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResponse, position: Int) {
            binding.user = user
            binding.onClickListener = clickListener
            binding.executePendingBindings()

            val cardBackgroundColor = when (position % 3) {
                0 -> {
                    val typedValue = TypedValue()
                    binding.root.context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true)
                    ContextCompat.getColorStateList(binding.root.context, typedValue.resourceId)
                }
                1 -> {
                    val typedValue = TypedValue()
                    binding.root.context.theme.resolveAttribute(com.google.android.material.R.attr.colorSecondaryContainer, typedValue, true)
                    ContextCompat.getColorStateList(binding.root.context, typedValue.resourceId)
                }
                2 -> {
                    val typedValue = TypedValue()
                    binding.root.context.theme.resolveAttribute(com.google.android.material.R.attr.colorTertiaryContainer, typedValue, true)
                    ContextCompat.getColorStateList(binding.root.context, typedValue.resourceId)
                }
                else -> {
                    val typedValue = TypedValue()
                    binding.root.context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true)
                    ContextCompat.getColorStateList(binding.root.context, typedValue.resourceId)
                }
            }
            binding.itemCardView.setCardBackgroundColor(cardBackgroundColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class OnUserItemClickListener(val clickListener: (username: String) -> Unit) {
    fun onClick(username: String) = clickListener(username)
}