package com.ibrahimss.githubgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ibrahimss.githubgeek.data.DefaultUserRepository
import com.ibrahimss.githubgeek.data.model.UserResponse

class UserFavoriteViewModel(userRepository: DefaultUserRepository): ViewModel() {

    val favoriteUsers: LiveData<List<UserResponse>> = userRepository.getAllUser()
}