package ru.devcold.pita

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

abstract class LoginFunctionsActivity: BaseActivity() {
    companion object {
        const val TAG = "LoginFunctionsActivity"
        private const val RC_SIGN_IN = 9001
    }

    protected val signedIn get() = Firebase.auth.currentUser != null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        regLoginListener()
    }

    protected fun launchLoginFlow() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = Firebase.auth.currentUser
                    getSPE().putString(USER_ID, user!!.uid).apply()
                    val newUser: Boolean = task.result?.additionalUserInfo!!.isNewUser
                    checkUser(newUser, user.uid)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    onLoginFailed()
                }
            }
    }

    protected fun onLoginSucceed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    protected fun checkUser(newUser: Boolean, userId: String) {
        if(newUser){
            createNewUser()
        }else {
            lifecycleScope.launch {
                val doc = database.collection("users").document(userId).get().await()

                if (doc.exists() && doc.contains("name")) {
                    toast("doc exist and have name")
                    getSPE().putString(USER_NAME, doc.getString("name")).apply()
                    onLoginSucceed()
                } else {
                    toast("doc not exist or doesn't contain name value. BaseActivity")
                    createNewUser()
                }
            }
        }
    }

    protected open fun onLoginFailed() {
        Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show()
    }

    private fun regLoginListener() {
        val authListener = Firebase.auth.addAuthStateListener {
        }
    }

    private fun createNewUser() {
        val intent = Intent(this, InitialProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
}