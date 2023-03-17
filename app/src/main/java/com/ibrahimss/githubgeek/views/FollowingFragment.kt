package com.ibrahimss.githubgeek.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.ibrahimss.githubgeek.adapter.OnUserItemClickListener
import com.ibrahimss.githubgeek.adapter.UserAdapter
import com.ibrahimss.githubgeek.databinding.FragmentFollowingBinding
import com.ibrahimss.githubgeek.util.OnUserItemCallback
import com.ibrahimss.githubgeek.util.viewModelsFactory
import com.ibrahimss.githubgeek.viewmodels.UserFollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private val viewModel: UserFollowingViewModel by viewModelsFactory {
        val userId = requireArguments().getString("username")!!
        UserFollowingViewModel(userId)
    }

    lateinit var onUserItemCallback: OnUserItemCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val userFollowingListAdapter = UserAdapter(OnUserItemClickListener {
            onUserItemCallback.onUserItemClicked(it)
            viewModel.fetchUserFollowing(it)
        })

        binding.rvFollowing.adapter = userFollowingListAdapter

        viewModel.userFollowing.observe(viewLifecycleOwner) {
            binding.ivNoData.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            binding.tvNoData.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.snackbarMessage.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() != null) {
                Snackbar.make(binding.root, it.peekContent(), Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.progressIndicator.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val username = (parentFragment as UserDetailFragment).username
        username?.let {
            viewModel.fetchUserFollowing(it)
        }
    }
}