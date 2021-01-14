package com.farida.coursework.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.farida.coursework.MyApplication
import com.farida.coursework.R
import com.farida.coursework.model.Post
import com.farida.coursework.model.UserResponse
import com.farida.coursework.ui.decorator.DividerPostDecoration
import com.farida.coursework.ui.postsList.OnBottomNavigationVisibilityListener
import kotlinx.android.synthetic.main.fragment_profile.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class ProfileFragment : MvpAppCompatFragment(), ProfileView  {

    private var bottomNavigationVisibilityListener: OnBottomNavigationVisibilityListener? = null
    private lateinit var profileAdapter: ProfileAdapter

    @Inject
    lateinit var profilePresenterProvider: Provider<ProfilePresenter>

    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter

    @ProvidePresenter
    fun providePresenter(): ProfilePresenter {
        return profilePresenterProvider.get()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as MyApplication).presenterComponent?.inject(this)
        if (context is OnBottomNavigationVisibilityListener) {
            bottomNavigationVisibilityListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileAdapter = ProfileAdapter(profilePresenter, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        bottomNavigationVisibilityListener?.onSetBottomNavigationVisibility(View.VISIBLE)
    }

    private fun initRecyclerView() {
        profileRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = profileAdapter
            addItemDecoration(DividerPostDecoration(context))
        }
    }

    override fun setUser(userResponse: UserResponse) {
        profileAdapter.userResponse = userResponse
        profileAdapter.notifyDataSetChanged()
    }

    override fun setOwnPosts(posts: List<Post>) {
        profileAdapter.setDataSource(posts)
    }

    override fun showErrorMessage(textResource: Int) {
        Toast.makeText(requireContext(), textResource, Toast.LENGTH_SHORT).show()
    }
}