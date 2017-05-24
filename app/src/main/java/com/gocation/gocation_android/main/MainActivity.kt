package com.gocation.gocation_android.main

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import android.util.Log
import co.zsmb.materialdrawerkt.builders.accountHeader
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.profile.profile
import com.facebook.login.LoginManager
import com.firststarcommunications.ampmscratchpower.android.adapters.UsersListAdapter
import com.gocation.gocation_android.*
import com.gocation.gocation_android.background.BackgroundBeaconService
import com.gocation.gocation_android.data.*
import com.gocation.gocation_android.login.LoginActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mikepenz.materialdrawer.Drawer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by dylanlange on 11/05/17.
 */

class MainActivity: AppCompatActivity() {
    val PERMISSION_REQUEST_CODE: Int = 69

    lateinit private var mUsers: List<User>
    lateinit private var mSharedPreferences: SharedPreferences
    lateinit private var mEditor: SharedPreferences.Editor
    lateinit private var mDrawer: Drawer
    lateinit var mBeaconServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBeaconServiceIntent = Intent(this@MainActivity, BackgroundBeaconService::class.java)
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        mEditor = mSharedPreferences.edit()

        setupActionBar()
        requestLocationPermissions()
        startService(mBeaconServiceIntent)

        var name: String = mSharedPreferences.getString(NAME_PREFS_KEY, "")
        var email: String = mSharedPreferences.getString(EMAIL_PREFS_KEY, "")
        var imageUrl: String = mSharedPreferences.getString(IMAGE_URL_PREFS_KEY, "")

        Picasso.with(this)
                .load(imageUrl)
                .into(iv_action_bar_image)

        mDrawer = drawer {
            accountHeader {
                background = R.color.material_blue_grey_900
                profile(name, email) {
                    iconUrl = imageUrl
                }
            }
            primaryItem("Log out") {
                icon = R.drawable.ic_logout
                onClick { _ ->
                    mEditor.putString(ID_PREFS_KEY, null)
                    mEditor.apply()
                    LoginManager.getInstance().logOut()
                    var intent: Intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    false
                }
            }
        }

    }

    private fun requestLocationPermissions() {
        val locationFineCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        val locationCoarseCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)

        if (locationFineCheck != PermissionChecker.PERMISSION_GRANTED
                && locationCoarseCheck != PermissionChecker.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    , PERMISSION_REQUEST_CODE)
        }
    }

    override fun onResume() {
        super.onResume()
        listenForAllUsers(object: ValueEventListener {

            override fun onCancelled(databaseError: DatabaseError?) { }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                mUsers = getAllUsersFromSnapshot(dataSnapshot)
                Log.d("DYLAN", mUsers.toString())
                listview.adapter = UsersListAdapter(this@MainActivity, R.layout.list_item_user, mUsers.toMutableList())
            }

        })

        listenForChangeToUsers(object: ChildEventListener{

            override fun onCancelled(databaseError: DatabaseError?) { }

            override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) { }

            override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                var changedUser: User = getUserFromSnapshot(dataSnapshot)
                //handle updating view here
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                var addedUser: User = getUserFromSnapshot(dataSnapshot)
                //handle updating view here
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
                var removedUser: User = getUserFromSnapshot(dataSnapshot)
                //handle updating view here
            }

        })
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        btn_menu.setOnClickListener { menuBtnClicked() }
        btn_alternate.setOnClickListener { alternateBtnClicked() }
    }

    private fun menuBtnClicked() {
        if(!mDrawer.isDrawerOpen)
            mDrawer.openDrawer()
        else
            mDrawer.closeDrawer()
    }

    private fun alternateBtnClicked() {

    }
}