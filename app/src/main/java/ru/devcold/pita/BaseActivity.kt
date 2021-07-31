package ru.devcold.pita

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ru.devcold.pita.login.LoginActivity

abstract class BaseActivity: AppCompatActivity() {
    companion object {
        const val TAG = "BaseActivity"
        private const val CLIENT_ID = "323850627789-npf8s0gkp8quk8a73a64dk5hbk7iqo8a.apps.googleusercontent.com"
    }

    protected lateinit var database: FirebaseFirestore

    protected lateinit var googleSignInClient: GoogleSignInClient

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.firestore

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENT_ID)
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    protected fun signOut() {
        Firebase.auth.signOut()
        googleSignInClient.signOut()
        getSP().edit().remove(USER_ID).apply()
        getSP().edit().remove(USER_NAME).apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}