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

class UserDetailViewModel(private val username: String): ViewModel() {

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse>
        get() = _user

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>>
        get() = _snackbarMessage

    init {
        fetchUserDetail()
    }

    fun fetchUserDetail(username: String = this.username) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val request = GithubApi.retrofitService.getUser(username)
            request.enqueue(object: Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        _user.postValue(response.body())
                    } else {
                        _snackbarMessage.value = Event("Something went wrong: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.postValue(false)
                    _snackbarMessage.value = Event("Something went wrong: ${t.message}")
                }
            })
        }
    }
}