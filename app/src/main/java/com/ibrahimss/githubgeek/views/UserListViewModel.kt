package com.ibrahimss.githubgeek.views

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimss.githubgeek.model.UserResponse
import com.ibrahimss.githubgeek.network.GithubApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel: ViewModel() {

    companion object {
        private const val TAG = "UserListViewModel"
    }

    private val _users = MutableLiveData<List<UserResponse>>()
    val users: LiveData<List<UserResponse>>
        get() = _users

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val client = GithubApi.retrofitService.getAllUsers()
            client.enqueue(object: Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    if (response.isSuccessful) {
                        _users.postValue(response.body())
                    } else {
                        Log.e(TAG, "onResponse: ${response.errorBody()}")
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                    _loading.value = false
                }
            })
        }
    }

    private var isSorted = false
    fun sortItemAlphabetically() {
        if (!isSorted) {
            _users.value = _users.value?.sortedBy { it.username }
        }
        else {
            _users.value = _users.value?.sortedBy { it.id }
        }

        isSorted = !isSorted
    }
}