package ru.devcold.pita

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.devcold.pita.databinding.ActivitySmsCodeBinding
import java.util.concurrent.TimeUnit

class SmsCodeActivity : BaseActivity() {
    private val binding: ActivitySmsCodeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_sms_code)
    }
    private lateinit var phoneNumber: String
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        setUpViews()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                toast(R.string.verification_complete)
                val smsCode = credential.smsCode
                if(smsCode != null) {
                    verifyCode(smsCode)
                } else
                    signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                Toast.makeText(this@SmsCodeActivity, e.message, Toast.LENGTH_LONG).show();
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                Log.d(TAG, "onCodeSent:$verificationId")
                toast(R.string.code_sent)
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }
        }
        sendVerificationCode()
    }

    private fun setUpViews() {
        phoneNumber = intent.getStringExtra("phoneNumber")!!
        val descriptionText = resources.getString(R.string.sms_code_activity_description, phoneNumber)
        val startIndex = descriptionText.indexOf(phoneNumber)
        val endIndex = startIndex + phoneNumber.length
        val spannable = SpannableString(descriptionText)
        spannable.setSpan(ForegroundColorSpan(Color.BLACK), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.description.text = spannable

        binding.next.setOnClickListener {
            if(binding.smsCode.editText!!.text.isNullOrEmpty()) {
                toast(R.string.enter_code)
            }else
              verifyCode(binding.smsCode.editText!!.text.toString())
        }

        /*binding.smsCode.editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.smsCode.editText!!.length() == 6) {
                    signInWithPhoneAuthCredential()
                }else
                    binding.smsCode.isErrorEnabled = true
                    binding.smsCode.error = resources.getString(R.string.enter_valid_code)
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.smsCode.isErrorEnabled)
                binding.smsCode.error = null
            }
        })*/

        lifecycleScope.launch {
            for (i in 59 downTo 0) {
                binding.countdown.text = resources.getString(R.string.countdown, i)
                delay(1000)
                if(i == 0) {
                    binding.countdown.visibility = View.INVISIBLE
                    binding.resendCode.visibility = View.VISIBLE
                }
            }
        }
        binding.resendCode.setOnClickListener {
            resendVerificationCode()
        }
    }

    private fun sendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: AuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    onLoginSucceed()
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Вы ввели неправильный код.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun verifyCode(smsCode: String) {
        val credential = storedVerificationId?.let { PhoneAuthProvider.getCredential(it, smsCode) }
        if (credential != null) {
            signInWithPhoneAuthCredential(credential)
        }
    }

    override fun onLoginSucceed() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun resendVerificationCode() {
        val optionsBuilder = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        optionsBuilder.setForceResendingToken(resendToken)
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
        Toast.makeText(this, "Код повторно отправлен", Toast.LENGTH_SHORT).show()

    }
}