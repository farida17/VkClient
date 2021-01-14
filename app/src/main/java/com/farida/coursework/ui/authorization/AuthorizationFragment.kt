package com.farida.coursework.ui.authorization

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.farida.coursework.R
import com.farida.coursework.ui.postsList.OnBottomNavigationVisibilityListener
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.fragment_authorization.*
import moxy.MvpAppCompatFragment

class AuthorizationFragment : MvpAppCompatFragment() {

    private var bottomNavigationVisibilityListener: OnBottomNavigationVisibilityListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBottomNavigationVisibilityListener) {
            bottomNavigationVisibilityListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authorization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationVisibilityListener?.onSetBottomNavigationVisibility(View.GONE)
        btn_login_enter.setOnClickListener {
            btn_login_enter.isClickable = false
            VK.login(requireActivity(), arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.FRIENDS))
        }
    }

    override fun onResume() {
        super.onResume()
        btn_login_enter.isClickable = true
    }
}