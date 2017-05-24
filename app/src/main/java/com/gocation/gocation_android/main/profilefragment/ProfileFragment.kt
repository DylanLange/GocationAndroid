package com.gocation.gocation_android.main.profilefragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gocation.gocation_android.EMAIL_PREFS_KEY
import com.gocation.gocation_android.IMAGE_URL_PREFS_KEY
import com.gocation.gocation_android.NAME_PREFS_KEY
import com.gocation.gocation_android.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * Created by dylanlange on 25/05/17.
 */

class ProfileFragment: android.support.v4.app.Fragment() {

    companion object {
        @JvmStatic fun newInstance() = ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        var imageUrl: String = prefs.getString(IMAGE_URL_PREFS_KEY, "")
        var name: String = prefs.getString(NAME_PREFS_KEY, "")
        var email: String = prefs.getString(EMAIL_PREFS_KEY, "")

        Picasso.with(activity)
                .load(imageUrl)
                .into(iv_profile_image)

        tv_profile_name.text = name
        tv_profile_email.text = email
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? =  inflater?.inflate(R.layout.fragment_profile, container, false)

        return view
    }

}