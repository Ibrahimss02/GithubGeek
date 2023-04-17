package com.ibrahimss.githubgeek.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ibrahimss.githubgeek.adapter.OnUserItemClickListener
import com.ibrahimss.githubgeek.R
import com.ibrahimss.githubgeek.adapter.UserAdapter
import com.ibrahimss.githubgeek.data.preferences.SettingPreferences
import com.ibrahimss.githubgeek.databinding.FragmentUserListBinding
import com.ibrahimss.githubgeek.viewmodels.UserListViewModel
import com.robinhood.ticker.TickerUtils

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserListFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentUserListBinding
    private val viewModel: UserListViewModel by viewModels()

    private var isShowingSearchTextInput = false

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

        viewModel.pref = SettingPreferences.getInstance(requireContext().dataStore)

        val adapter = UserAdapter(OnUserItemClickListener { username ->
            findNavController().navigate(
                UserListFragmentDirections.actionUserListFragmentToUserDetailFragment(
                    username
                )
            )
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

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.searchUser(it)
            } else {
                viewModel.fetchUsers()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
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

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnDarkMode.visibility = View.GONE
                binding.btnLightMode.visibility = View.VISIBLE
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnDarkMode.visibility = View.VISIBLE
                binding.btnLightMode.visibility = View.GONE
            }
        }

        binding.btnFavorite.setOnClickListener(this)
        binding.btnSearch.setOnClickListener(this)
        binding.btnLightMode.setOnClickListener(this)
        binding.btnDarkMode.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_search -> {
                isShowingSearchTextInput = !isShowingSearchTextInput
                if (isShowingSearchTextInput) {
                    binding.tilSearch.visibility = View.VISIBLE
                    binding.etSearch.requestFocus()
                } else {
                    binding.tilSearch.visibility = View.GONE
                }
            }
            R.id.btn_favorite -> findNavController().navigate(UserListFragmentDirections.actionUserListFragmentToUserFavoriteFragment())
            R.id.btn_dark_mode -> viewModel.saveThemeSetting(true)
            R.id.btn_light_mode -> viewModel.saveThemeSetting(false)
        }
    }
}