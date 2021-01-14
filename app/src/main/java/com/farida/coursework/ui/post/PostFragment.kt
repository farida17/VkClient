package com.farida.coursework.ui.post

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.farida.coursework.MyApplication
import com.farida.coursework.R
import com.farida.coursework.model.Comment
import com.farida.coursework.model.Post
import com.farida.coursework.ui.decorator.DividerPostDecoration
import com.farida.coursework.ui.error.ErrorFragment
import com.farida.coursework.ui.postsList.OnBottomNavigationVisibilityListener
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_post.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider

class PostFragment : MvpAppCompatFragment(), PostView, OnCreateCommentButtonListener, OnShareImageListener, OnSaveImageListener {

    private var bottomNavigationVisibilityListener: OnBottomNavigationVisibilityListener? = null
    private lateinit var post: Post
    private lateinit var postAdapter: PostAdapter
    private var newImageFile: File? = null

    @Inject
    lateinit var postPresenterProvider: Provider<PostPresenter>

    @InjectPresenter
    lateinit var postPresenter: PostPresenter

    @ProvidePresenter
    fun providePresenter(): PostPresenter {
        return postPresenterProvider.get()
    }

    override fun onAttach(context: Context) {
        (activity?.applicationContext as MyApplication).presenterComponent?.inject(this)
        super.onAttach(context)
        if (context is OnBottomNavigationVisibilityListener) {
            bottomNavigationVisibilityListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postAdapter = PostAdapter(this, this, this,  requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationVisibilityListener?.onSetBottomNavigationVisibility(View.GONE)
        post = requireArguments().getParcelable(ARG_OPEN_POST_KEY)!!
        postPresenter.loadPost(post)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        postRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
            addItemDecoration(DividerPostDecoration(context))
        }
    }

    private fun createNewImageUri(photo: Bitmap): Uri? {
        val date = System.currentTimeMillis().toString()
        val newImage = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, date)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/JPEG")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        val newImageUri: Uri? = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            newImage
        )

        try {
            compressPhoto(newImageUri, photo)
        } catch (e: IOException) {
            if (newImageUri != null) {
                requireContext().contentResolver.delete(newImageUri, null, null)
            }
            throw IOException(e)
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                newImage.put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        return newImageUri
    }

    private fun compressPhoto(uri: Uri?, photo: Bitmap) =
        uri?.let {
            val stream = requireContext().contentResolver.openOutputStream(uri)
            if (!photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                throw IOException(requireContext().getString(R.string.error_save_bitmap))
            }
        } ?: throw IOException(requireContext().getString(R.string.error_create_media_store_record))

    override fun shareImage(photo: Bitmap) {
        if (checkPermission()) {
        val uri = createNewImageUri(photo)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/jpeg"
        }
        startActivityForResult(
            Intent.createChooser(shareIntent, resources.getText(R.string.send_to)), SENDING_REQUEST_CODE)
        } else {
            requestPermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SENDING_REQUEST_CODE) {
            newImageFile?.delete()
        }
    }

    override fun saveImage(photo: Bitmap) {
        if (checkPermission()) {
            createNewImageUri(photo)
            Toast.makeText(requireContext(), R.string.savingImage, Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
    }

    override fun setLoading(isLoading: Boolean) {
        shimmerViewContainer.isVisible = true
        shimmerViewContainer.startShimmer()
        Thread.sleep(1000)
        shimmerViewContainer.stopShimmer()
        shimmerViewContainer.isGone = true
    }

    override fun setOpenPost(post: Post) {
        postAdapter.post = post
        postAdapter.notifyDataSetChanged()
    }

    override fun setComments(comments: List<Comment>) {
        postAdapter.setDataSource(comments)
    }

    override fun showErrorFragment() {
        val dialog = ErrorFragment()
        childFragmentManager.beginTransaction()
            .add(dialog, ERROR_FRAGMENT_TAG)
            .commit()
    }

    private fun checkPermission(): Boolean {
        val result = checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) {
            Toast.makeText(
                requireContext(),
                R.string.writeExternalStoragePermission,
                Toast.LENGTH_LONG
            ).show()
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    requireContext(),
                    R.string.permissionGranted,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.permissionDenied,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun showErrorMessage(textResource: Int) {
        Toast.makeText(requireContext(), textResource, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateCommentButtonClick(message: String) {
        postPresenter.onCreateComment(post.sourceId, post.postId, message)
    }

    companion object {

        private const val SENDING_REQUEST_CODE = 1
        const val PERMISSION_REQUEST_CODE = 1
        const val ERROR_FRAGMENT_TAG = "error"
        private const val ARG_OPEN_POST_KEY = "open_post_key"

        fun newInstance(post: Post): PostFragment = PostFragment().apply {
            arguments = bundleOf(ARG_OPEN_POST_KEY to post)
        }
    }
}