package com.ibrahimss.githubgeek.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ibrahimss.githubgeek.adapter.OnUserItemClickListener
import com.ibrahimss.githubgeek.R
import com.ibrahimss.githubgeek.adapter.UserAdapter
import com.ibrahimss.githubgeek.databinding.FragmentUserListBinding
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
        binding.lifecycleOwner = this

        val adapter = UserAdapter(OnUserItemClickListener {
            // TODO: Navigate to User Detail
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
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvGreetProgress.text = getString(R.string.greet_loading)
            } else {
                binding.progressIndicator.visibility = View.GONE
                binding.tvGreetProgress.text = "Looking for someone? ^_____^"
            }
        }
    }
}