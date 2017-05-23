package com.gocation.gocation_android.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import com.gocation.gocation_android.R
import com.gocation.gocation_android.data.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mikepenz.materialdrawer.Drawer

/**
 * Created by dylanlange on 11/05/17.
 */

class MainActivity: AppCompatActivity() {

    private var mUsers: List<User>? = null
    lateinit private var mDrawer: Drawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()

        mDrawer = drawer {
            primaryItem("Logout") {}
        }
    }

    override fun onResume() {
        super.onResume()
        listenForAllUsers(object: ValueEventListener {

            override fun onCancelled(databaseError: DatabaseError?) { }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                mUsers = getAllUsersFromSnapshot(dataSnapshot)
                Log.d("DYLAN", mUsers.toString())
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
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val mInflater = LayoutInflater.from(this)

        val customView = mInflater.inflate(R.layout.generic_action_bar, null)
        customView.findViewById(R.id.btn_menu).setOnClickListener { menuBtnClicked() }
        customView.findViewById(R.id.btn_alternate).setOnClickListener { alternateBtnClicked() }

        supportActionBar?.customView = customView
        supportActionBar?.setDisplayShowCustomEnabled(true)
    }

    private fun menuBtnClicked() {
        mDrawer.openDrawer()
    }

    private fun alternateBtnClicked() {

    }
}