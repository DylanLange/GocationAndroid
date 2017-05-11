package com.gocation.gocation_android.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.gocation.gocation_android.*
import com.gocation.gocation_android.data.User
import com.gocation.gocation_android.main.MainActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException

/**
 * Created by dylanlange on 11/05/17.
 */

class LoginActivity: AppCompatActivity() {

    lateinit var mCallbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if(prefs.getString(ID_PREFS_KEY, "").equals("")){
            //id hasnt been saved to preferences. safe to say that the user isn't logged in.
        } else {
            //logged in already so just go to main activity
            goToMainActivity()
        }

        mCallbackManager = CallbackManager.Factory.create()
        btn_login.setReadPermissions("public_profile", "email")
        btn_login.registerCallback(mCallbackManager, object: FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                if(result == null) return
                val request = GraphRequest.newMeRequest(result.accessToken)
                { returnedObject, _ ->
                    try {
                        val id = returnedObject.getString("id")
                        val name = returnedObject.getString("name")
                        val email = returnedObject.getString("email")
                        val gender = returnedObject.getString("gender")
                        val ageRange = returnedObject.getString("age_range")

                        signInFacebookUser(id, name, email, gender, ageRange)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,gender,age_range")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
//        var profile: Profile = Profile.getCurrentProfile()
//        signInToFirebase(profile.id, profile.name)
    }

    private fun signInFacebookUser(id: String, name: String, email: String, gender: String, ageRange: String) {
        signInToFirebase(id, name, email, gender, ageRange)
        var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor: SharedPreferences.Editor = prefs.edit()

        editor.putString(ID_PREFS_KEY, id)
        editor.putString(NAME_PREFS_KEY, name)
        editor.putString(EMAIL_PREFS_KEY, email)
        editor.putString(GENDER_PREFS_KEY, gender)
        editor.putString(AGE_RANGE_PREFS_KEY, ageRange)

        editor.apply()

        goToMainActivity()
    }

    private fun signInToFirebase(id: String, name: String, email: String, gender: String, ageRange: String){

        FirebaseDatabase.getInstance().getReference("users").child(id).setValue(
                User(id, name, email.toFirebaseKey(), gender, ageRange)
        )
//        mFirebaseDatabase.child(formatEmail("daniel@paperkite.co.nz")).setValue(User(formatEmail("daniel@paperkite.co.nz"), "Daniel Grey", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("will@paperkite.co.nz")).setValue(User(formatEmail("will@paperkite.co.nz"), "Will Townsend", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("langedyla@gmail.com")).setValue(User(formatEmail("langedyla@gmail.com"), "Dylan Lange", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite0.co.nz")).setValue(User(formatEmail("test@paperkite0.co.nz"), "Test 0", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite1.co.nz")).setValue(User(formatEmail("test@paperkite1.co.nz"), "Test 1", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite2.co.nz")).setValue(User(formatEmail("test@paperkite2.co.nz"), "Test 2", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite3.co.nz")).setValue(User(formatEmail("test@paperkite3.co.nz"), "Test 3", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite4.co.nz")).setValue(User(formatEmail("test@paperkite4.co.nz"), "Test 4", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite5.co.nz")).setValue(User(formatEmail("test@paperkite5.co.nz"), "Test 5", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite6.co.nz")).setValue(User(formatEmail("test@paperkite6.co.nz"), "Test 6", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite7.co.nz")).setValue(User(formatEmail("test@paperkite7.co.nz"), "Test 7", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite8.co.nz")).setValue(User(formatEmail("test@paperkite8.co.nz"), "Test 8", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite9.co.nz")).setValue(User(formatEmail("test@paperkite9.co.nz"), "Test 9", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite10.co.nz")).setValue(User(formatEmail("test@paperkite10.co.nz"), "Test 10", true, getRandomColor()))
//        mFirebaseDatabase.child(formatEmail("test@paperkite11.co.nz")).setValue(User(formatEmail("test@paperkite11.co.nz"), "Test 11", true, getRandomColor()))

        //need to request permissions at runtime. go to permissions screen
//        mView.goToPermissions()
}

    private fun goToMainActivity(){
        var i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

}