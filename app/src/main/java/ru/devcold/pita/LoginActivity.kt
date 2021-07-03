package ru.devcold.pita

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.SignInButton
import ru.devcold.pita.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_login)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        setUpViews()

        if(signedIn)
            onLoginSucceed()
    }

    override fun onLoginSucceed() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setUpViews() {
        binding.signInButton.setSize(SignInButton.SIZE_WIDE)
        binding.signInButton.setOnClickListener {
            launchLoginFlow()
        }
    }
}