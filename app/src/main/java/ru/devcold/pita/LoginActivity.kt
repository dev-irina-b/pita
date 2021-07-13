package ru.devcold.pita

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        binding.signIn.setSize(SignInButton.SIZE_WIDE)
        binding.signIn.setOnClickListener {
            launchLoginFlow()
        }

        binding.phoneSignIn.setOnClickListener {
            val userInput = binding.phoneNumber.editText!!.text.toString()

            if (userInput.length == 10) {
                val countryCode = binding.phoneNumber.prefixText!!
                val phoneNumber = "$countryCode$userInput"
                val intent = Intent(this, SmsCodeActivity::class.java)
                intent.putExtra("phoneNumber", phoneNumber)
                startActivity(intent)
                finish()
            } else {
                binding.phoneNumber.isErrorEnabled = true
                binding.phoneNumber.error = resources.getString(R.string.enter_phone_number)
            }
        }
        binding.phoneNumber.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.phoneNumber.isErrorEnabled)
                    binding.phoneNumber.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}