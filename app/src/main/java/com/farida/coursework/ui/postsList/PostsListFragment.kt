package com.farida.coursework.ui.postsList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.farida.coursework.MyApplication
import com.farida.coursework.R
import com.farida.coursework.model.Post
import com.farida.coursework.ui.decorator.DividerPostDecoration
import com.farida.coursework.ui.error.ErrorFragment
import com.farida.coursework.ui.touchHelper.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.fragment_news.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class PostsListFragment: MvpAppCompatFragment(), PostListAdapter.OnPostClickListener, PostsListView {

    private lateinit var postsListAdapter: PostListAdapter
    private var onOpenPostClickListener: OnOpenPostClickListener? = null
    private var bottomNavigationVisibilityListener: OnBottomNavigationVisibilityListener? = null
    private lateinit var touchHelper: ItemTouchHelper

    @Inject
    lateinit var listPresenterProvider: Provider<PostsListPresenter>

    @InjectPresenter
    lateinit var listPresenter: PostsListPresenter

    @ProvidePresenter
    fun providePresenter(): PostsListPresenter {
        return listPresenterProvider.get()
    }

    override fun onAttach(context: Context) {
        (activity?.applicationContext as MyApplication).presenterComponent?.inject(this)
        super.onAttach(context)
        if (context is OnOpenPostClickListener) {
            onOpenPostClickListener = context
        }
        if (context is OnBottomNavigationVisibilityListener) {
            bottomNavigationVisibilityListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postsListAdapter = PostListAdapter(this, listPresenter, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        bottomNavigationVisibilityListener?.onSetBottomNavigationVisibility(View.VISIBLE)
    }

    private fun initRecyclerView() {
        listRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsListAdapter
            addItemDecoration(DividerPostDecoration(context))
        }
        val callback = ItemTouchHelperCallback(postsListAdapter, requireContext())
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(listRecyclerView)
        swipeContainer.setOnRefreshListener {
            listPresenter.loadDbPosts()
            swipeContainer.isRefreshing = false
        }
    }

    override fun setPostsList(posts: List<Post>) {
        postsListAdapter.setDataSource(posts)
    }

    override fun showErrorFragment() {
        val dialog = ErrorFragment()
        childFragmentManager.beginTransaction()
            .add(dialog, ERROR_FRAGMENT_TAG)
            .commit()
    }

    override fun showErrorMessage(textResource: Int) {
        Toast.makeText(requireContext(), textResource, Toast.LENGTH_SHORT).show()
    }

    override fun onPostClick(adapterPosition: Int) {
        onOpenPostClickListener?.onSendPost(postsListAdapter.posts[adapterPosition])
    }

    private companion object {
        const val ERROR_FRAGMENT_TAG = "error"
    }
}