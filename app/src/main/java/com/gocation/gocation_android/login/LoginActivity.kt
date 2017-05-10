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
import com.gocation.gocation_android.main.MainActivity
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

                        signInFBUser(id, name, email, gender, ageRange)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }

        })
    }

    private fun signInFBUser(id: String, name: String, email: String, gender: String, ageRange: String) {
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

    private fun goToMainActivity(){
        var i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

}