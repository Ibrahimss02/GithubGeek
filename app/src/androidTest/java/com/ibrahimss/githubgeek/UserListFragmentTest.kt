package com.ibrahimss.githubgeek

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test case terkadang failed karena Animation Scale pada perangkat
 */
@RunWith(AndroidJUnit4::class)
class UserListFragmentTest {

    /**
     * Username Github hanya boleh merupakan alphanumeric dan dash (-)
     */
    private val randomImpossibleUsername = "**"

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertNoUserFoundWithImpossibleUsername() {
        onView(withId(R.id.btn_search)).perform(click())
        onView(withId(R.id.til_search)).check(matches(isDisplayed()))
        onView(withId(R.id.et_search)).perform(typeText(randomImpossibleUsername), closeSoftKeyboard())

        // menggunakan withContentDescription karena CustomView yang nilai Text-nya ada pada attr contentDescription
        onView(withId(R.id.tv_count_user)).check(matches(withContentDescription("0 user(s)")))
    }
}