package com.farida.coursework.ui.likePostsList

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
import com.farida.coursework.ui.decorator.DividerPostDecoration
import kotlinx.android.synthetic.main.fragment_like_posts_list.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class LikePostsListFragment : MvpAppCompatFragment(), LikePostView {

    private lateinit var likePostAdapter: LikePostsListAdapter

    @Inject
    lateinit var likePostsListPresenterProvider: Provider<LikePostsListPresenter>

    @InjectPresenter
    lateinit var likePostsListPresenter: LikePostsListPresenter

    @ProvidePresenter
    fun providePresenter(): LikePostsListPresenter {
        return likePostsListPresenterProvider.get()
    }

    override fun onAttach(context: Context) {
        (activity?.applicationContext as MyApplication).presenterComponent?.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        likePostAdapter = LikePostsListAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_like_posts_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        likeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = likePostAdapter
            addItemDecoration(DividerPostDecoration(context))
        }
    }

    override fun setLikePostsList(likePosts: List<Post>) {
        likePostAdapter.setDataSource(likePosts)
    }

    override fun setErrorMessage(textResource: Int) {
        Toast.makeText(requireContext(), textResource, Toast.LENGTH_SHORT).show()
    }
}