package com.ibrahimss.githubgeek.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ibrahimss.githubgeek.data.DefaultUserRepository
import com.ibrahimss.githubgeek.data.model.UserResponse
import com.ibrahimss.githubgeek.data.remote.GithubApi
import com.ibrahimss.githubgeek.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(private val userRepository: DefaultUserRepository, application: Application, private val username: String) :
    AndroidViewModel(application) {

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse>
        get() = _user

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>>
        get() = _snackbarMessage

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    init {
        fetchUserDetail()
    }

    fun fetchUserDetail(username: String = this.username) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val request = GithubApi.retrofitService.getUser(username)
            request.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        _user.postValue(response.body())
                        checkIfUserIsFavorite(response.body()!!.id)
                    } else {
                        _snackbarMessage.value =
                            Event("Something went wrong: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.postValue(false)
                    _snackbarMessage.value = Event("Something went wrong: ${t.message}")
                }
            })
        }
    }

    fun addToFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(_user.value!!)
            withContext(Dispatchers.Main) {
                _isFavorite.value = true
            }
        }
    }

    fun deleteUserFromFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.delete(_user.value!!)
            withContext(Dispatchers.Main) {
                _isFavorite.value = false
            }
        }
    }

    private fun checkIfUserIsFavorite(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isFavorite.postValue(userRepository.getUserFavoriteState(userId))
        }
    }
}