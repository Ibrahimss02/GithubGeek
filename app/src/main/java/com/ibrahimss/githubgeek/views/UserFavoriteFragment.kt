package com.ibrahimss.githubgeek.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ibrahimss.githubgeek.adapter.OnUserItemClickListener
import com.ibrahimss.githubgeek.adapter.UserAdapter
import com.ibrahimss.githubgeek.data.DefaultUserRepository
import com.ibrahimss.githubgeek.data.local.UserDataSource
import com.ibrahimss.githubgeek.data.local.UserDatabase
import com.ibrahimss.githubgeek.databinding.FragmentUserFavoriteBinding
import com.ibrahimss.githubgeek.util.viewModelsFactory
import com.ibrahimss.githubgeek.viewmodels.UserFavoriteViewModel
import com.robinhood.ticker.TickerUtils

class UserFavoriteFragment : Fragment() {

    private lateinit var binding: FragmentUserFavoriteBinding
    private val viewModel: UserFavoriteViewModel by viewModelsFactory {
        val db = UserDatabase.getDatabase(requireContext())
        val userDataSource = UserDataSource(db.userDao())
        val repository = DefaultUserRepository(userDataSource)
        UserFavoriteViewModel(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.rvFavoriteUsers.adapter = UserAdapter(OnUserItemClickListener {
            findNavController().navigate(UserFavoriteFragmentDirections.actionUserFavoriteFragmentToUserDetailFragment(it))
        })

        binding.tvCountFavoriteUser.setCharacterLists(TickerUtils.provideNumberList())

        binding.apply {
            var isShow = true
            var scrollRange = -1
            appBarLayout.addOnOffsetChangedListener { barLayout, verticalOffset ->
                if (scrollRange == -1) {
                    scrollRange = barLayout?.totalScrollRange!!
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = "Favorite User"
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = " "
                    isShow = false
                }
            }
        }
    }
}