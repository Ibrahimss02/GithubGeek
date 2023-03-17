package com.ibrahimss.githubgeek.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ibrahimss.githubgeek.util.OnUserItemCallback
import com.ibrahimss.githubgeek.views.FollowersFragment
import com.ibrahimss.githubgeek.views.FollowingFragment

class UserDetailPagerAdapter(
    fragment: Fragment,
    private val username: String,
    private val onUserItemClickCallback: OnUserItemCallback
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> FollowingFragment().apply {
                onUserItemCallback = onUserItemClickCallback
            }
            1 -> FollowersFragment().apply {
                onUserItemCallback = onUserItemClickCallback
            }
            else -> FollowingFragment()
        }.apply {
            arguments = Bundle().apply {
                putString("username", username)
            }
        }
        return fragment
    }
}