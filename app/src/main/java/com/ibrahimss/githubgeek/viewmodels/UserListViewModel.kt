package com.ibrahimss.githubgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimss.githubgeek.model.SearchResponse
import com.ibrahimss.githubgeek.model.UserResponse
import com.ibrahimss.githubgeek.network.GithubApi
import com.ibrahimss.githubgeek.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel: ViewModel() {

    private val _users = MutableLiveData<List<UserResponse>>()
    val users: LiveData<List<UserResponse>>
        get() = _users

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _showHeadlineMessage = MutableLiveData<Event<Boolean>>()
    val showHeadlineMessage: LiveData<Event<Boolean>>
        get() = _showHeadlineMessage

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>>
        get() = _snackbarMessage

    var searchQuery = MutableLiveData<String>()

    init {
        _showHeadlineMessage.value = Event(false)
        fetchUsers()
    }

    fun fetchUsers() {
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
                        _showHeadlineMessage.value = Event(true)
                    } else {
                        _snackbarMessage.value = Event("Something went wrong: ${response.errorBody()}")
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    _snackbarMessage.value = Event("Something went wrong: ${t.message}")
                    _loading.value = false
                }
            })
        }
    }

    fun searchUser(query: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val client = GithubApi.retrofitService.searchUsers(query)
            client.enqueue(object: Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
                        _users.postValue(response.body()?.items)
                        _showHeadlineMessage.value = Event(true)
                    } else {
                        _snackbarMessage.value = Event("Something went wrong: ${response.errorBody()}")
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    _snackbarMessage.value = Event("Something went wrong: ${t.message}")
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