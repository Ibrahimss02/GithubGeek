package com.ibrahimss.githubgeek.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.ibrahimss.githubgeek.R
import com.ibrahimss.githubgeek.adapter.UserDetailPagerAdapter
import com.ibrahimss.githubgeek.databinding.FragmentUserDetailBinding
import com.ibrahimss.githubgeek.util.OnUserItemCallback
import com.ibrahimss.githubgeek.util.viewModelsFactory
import com.ibrahimss.githubgeek.viewmodels.UserDetailViewModel


class UserDetailFragment : Fragment() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.label_following,
            R.string.label_followers
        )
    }

    private lateinit var binding: FragmentUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModelsFactory {
        val username = requireArguments().getString("username")!!
        UserDetailViewModel(username)
    }

    var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val usernameArg = requireArguments().getString("username")!!

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }

        var usernameView: String? = null

        viewModel.user.observe(viewLifecycleOwner) {
            usernameView = it.name ?: it.username
            this.username = it.username
        }

        viewModel.snackbarMessage.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() != null) {
                Snackbar.make(binding.root, it.peekContent(), Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.apply {
            var isShow = true
            var scrollRange = -1
            appBarLayout.addOnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) {
                    scrollRange = barLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = usernameView ?: "User Detail"
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = " "
                    isShow = false
                }
            }
        }

        val userDetailPagerAdapter = UserDetailPagerAdapter(this, usernameArg, object: OnUserItemCallback {
            override fun onUserItemClicked(username: String) {
                viewModel.fetchUserDetail(username)
            }
        })

        val viewPager = binding.viewPager
        viewPager.adapter = userDetailPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.btnVisitProfile.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://github.com/${this.username}")
            startActivity(i)
        }
    }
}