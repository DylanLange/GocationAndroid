package com.gocation.gocation_android.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gocation.gocation_android.data.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

/**
 * Created by dylanlange on 11/05/17.
 */

class MainActivity: AppCompatActivity() {

    private var mUsers: List<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        listenForAllUsers(object: ValueEventListener {

            override fun onCancelled(databaseError: DatabaseError?) { }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                mUsers = getAllUsersFromSnapshot(dataSnapshot)
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

}