package com.ibrahimss.githubgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimss.githubgeek.model.UserResponse
import com.ibrahimss.githubgeek.network.GithubApi
import com.ibrahimss.githubgeek.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFollowersViewModel(private val username: String): ViewModel() {

    private val _userFollowers = MutableLiveData<List<UserResponse>>()
    val userFollowers: LiveData<List<UserResponse>>
        get() = _userFollowers

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>>
        get() = _snackbarMessage

    init {
        fetchUserFollowers()
    }

    fun fetchUserFollowers(username: String = this.username) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val request = GithubApi.retrofitService.getFollowers(username)
            request.enqueue(object: Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        _userFollowers.postValue(response.body())
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