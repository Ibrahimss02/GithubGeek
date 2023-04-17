package com.ibrahimss.githubgeek

import com.ibrahimss.githubgeek.data.DefaultUserRepository
import com.ibrahimss.githubgeek.data.model.UserResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private val dummyUser = mutableListOf(
        UserResponse(
            0,
            "dummy_user1",
            "Dummy User 1",
            "Dummy User 1 Bio",
            avatarUrl = "link_avatar",
            htmlUrl = "link_html",
            url = "user_url"
        ),
        UserResponse(
            1,
            "dummy_user2",
            "Dummy User 2",
            "Dummy User 2 Bio",
            avatarUrl = "link_avatar",
            htmlUrl = "link_html",
            url = "user_url"
        ),
        UserResponse(
            2,
            "dummy_user3",
            "Dummy User 3",
            "Dummy User 3 Bio",
            avatarUrl = "link_avatar",
            htmlUrl = "link_html",
            url = "user_url"
        )
    )

    private lateinit var userDataSource: FakeUserDataSource
    private lateinit var userRepository: DefaultUserRepository

    @Before
    fun setupRepository() {
        userDataSource = FakeUserDataSource(dummyUser)

        userRepository = DefaultUserRepository(userDataSource)
    }

    @Test
    fun getAllUsers_requestAllUserFromDataSource() {
        val users = userRepository.getAllUser().value

        assertNotNull(users)
        assertEquals(users, dummyUser)
    }

    @Test
    fun getUser_requestUserFromDataSource() {
        val user = userRepository.getUser("dummy_user1").value

        assertNotNull(user)
        assertEquals(user, dummyUser[0])
    }
}