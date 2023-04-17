package com.ibrahimss.githubgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimss.githubgeek.data.model.UserResponse
import com.ibrahimss.githubgeek.data.remote.GithubApi
import com.ibrahimss.githubgeek.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFollowingViewModel(private val username: String): ViewModel() {

    private val _userFollowing = MutableLiveData<List<UserResponse>>()
    val userFollowing: LiveData<List<UserResponse>>
        get() = _userFollowing

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>>
        get() = _snackbarMessage

    init {
        fetchUserFollowing()
    }

    fun fetchUserFollowing(username: String = this.username) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val request = GithubApi.retrofitService.getFollowing(username)
            request.enqueue(object: Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        _userFollowing.postValue(response.body())
                    } else {
                        _snackbarMessage.value = Event("Something went wrong: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    _isLoading.postValue(false)
                    _snackbarMessage.value = Event("Something went wrong: ${t.message}")
                }
            })
        }
    }
}