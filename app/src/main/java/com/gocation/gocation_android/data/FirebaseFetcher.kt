package com.gocation.gocation_android.data

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by dylanlange on 15/05/17.
 */

fun listenForAllUsers(listener: ValueEventListener) {
    FirebaseDatabase.getInstance().reference.child("users").addValueEventListener(listener)
}

fun listenForChangeToUsers(listener: ChildEventListener) {
    FirebaseDatabase.getInstance().reference.child("users").addChildEventListener(listener)
}

fun getAllUsersFromSnapshot(dataSnapshot: DataSnapshot?): List<User> = extractUsersFromRawMap(dataSnapshot!!.value as Map<String, Any>)

fun getUserFromSnapshot(dataSnapshot: DataSnapshot?): User = extractSingleUser(dataSnapshot!!.value as Map<*, *>)

fun extractUsersFromRawMap(map: Map<String, Any>): List<User> {
    var users: List<User> = emptyList()
    for((_, value) in map) {
        val singleUser = value as Map<*, *>
        users = users.plus(extractSingleUser(singleUser))
    }
    return users
}

fun extractSingleUser(rawUser: Map<*, *>): User= User(
        rawUser["id"] as String,
        rawUser["name"] as String,
        rawUser["email"] as String,
        rawUser["gender"] as String,
        rawUser["ageRange"] as String,
        rawUser["imageUrl"] as String
)