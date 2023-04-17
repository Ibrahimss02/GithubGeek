package com.ibrahimss.githubgeek.util

import androidx.recyclerview.widget.DiffUtil
import com.ibrahimss.githubgeek.data.model.UserResponse

class UserDiffUtilCallback: DiffUtil.ItemCallback<UserResponse>() {
    override fun areItemsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
        return oldItem.username == newItem.username
    }
}