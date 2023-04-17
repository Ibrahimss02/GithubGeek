package com.ibrahimss.githubgeek.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.ibrahimss.githubgeek.R
import com.ibrahimss.githubgeek.adapter.UserDetailPagerAdapter
import com.ibrahimss.githubgeek.data.DefaultUserRepository
import com.ibrahimss.githubgeek.data.local.UserDataSource
import com.ibrahimss.githubgeek.data.local.UserDatabase
import com.ibrahimss.githubgeek.databinding.FragmentUserDetailBinding
import com.ibrahimss.githubgeek.util.OnUserItemCallback
import com.ibrahimss.githubgeek.util.viewModelsFactory
import com.ibrahimss.githubgeek.viewmodels.UserDetailViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UserDetailFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModelsFactory {
        val db = UserDatabase.getDatabase(requireContext())
        val userDataSource = UserDataSource(db.userDao())
        val repository = DefaultUserRepository(userDataSource)
        val username = requireArguments().getString("username")!!
        UserDetailViewModel(repository, requireActivity().application, username)
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

        val userDetailPagerAdapter =
            UserDetailPagerAdapter(this, usernameArg, object : OnUserItemCallback {
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

        binding.btnVisitProfile.setOnClickListener(this)
        binding.fabFavorite.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_visit_profile -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://github.com/${this.username}")
                startActivity(i)
            }

            R.id.fab_favorite -> {
                Snackbar.make(binding.root, "Favorite", Snackbar.LENGTH_SHORT).apply {
                    if (viewModel.isFavorite.value == true) {
                        viewModel.deleteUserFromFavorite()
                        this.setText("User deleted from favorite")
                    } else {
                        viewModel.addToFavorite()
                        this.setText("User added to favorite")
                    }
                }
                    .show()
            }

            R.id.btn_share -> {
                Glide.with(this)
                    .asBitmap()
                    .load(viewModel.user.value?.avatarUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "image/*"
                                putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource))
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Hey, you better check out this guy on Github. ${viewModel.user.value?.username}\n\nhttps://github.com/${viewModel.user.value?.username}"
                                )
                            }
                            startActivity(Intent.createChooser(intent, "Share Image"))
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}

                    })

            }
        }
    }

    /**
     * Function to get the URI of the bitmap fetched from url by Glide
     */
    private fun getLocalBitmapUri(bitmap: Bitmap): Uri? {
        var bitmapUri: Uri? = null
        try {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_github_geek_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bitmapUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().packageName + ".provider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmapUri
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.label_following,
            R.string.label_followers
        )
    }
}