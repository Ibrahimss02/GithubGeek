package com.ibrahimss.githubgeek.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ibrahimss.githubgeek.adapter.OnUserItemClickListener
import com.ibrahimss.githubgeek.R
import com.ibrahimss.githubgeek.adapter.UserAdapter
import com.ibrahimss.githubgeek.databinding.FragmentUserListBinding
import com.ibrahimss.githubgeek.viewmodels.UserListViewModel
import com.robinhood.ticker.TickerUtils

class UserListFragment : Fragment() {

    private lateinit var binding: FragmentUserListBinding
    private val viewModel: UserListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = UserAdapter(OnUserItemClickListener { username ->
            findNavController().navigate(UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(username))
        })

        binding.apply {
            rvUsers.adapter = adapter
            viewModel = this@UserListFragment.viewModel
            tvCountUser.setCharacterLists(TickerUtils.provideNumberList())
            tvGreetProgress.setCharacterLists(TickerUtils.provideAlphabeticalList())

            var isShow = true
            var scrollRange = -1
            appBarLayout.addOnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) {
                    scrollRange = barLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = "Github Geek"
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = " "
                    isShow = false
                }
            }

            var isShowingSearchTextInput = false
            btnSearch.setOnClickListener {
                isShowingSearchTextInput = !isShowingSearchTextInput
                if (isShowingSearchTextInput) {
                    binding.tilSearch.visibility = View.VISIBLE
                    binding.etSearch.requestFocus()
                } else {
                    binding.tilSearch.visibility = View.GONE
                }
            }
        }

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.searchUser(it)
            } else {
                viewModel.fetchUsers()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.progressIndicator.visibility = View.GONE
            }
        }

        viewModel.showHeadlineMessage.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() == true) {
                binding.tvGreetProgress.text = getString(R.string.greet_loading_success)
            }
        }

        viewModel.snackbarMessage.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() != null) {
                Snackbar.make(binding.root, it.peekContent(), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}