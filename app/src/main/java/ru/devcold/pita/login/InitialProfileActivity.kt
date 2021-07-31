package ru.devcold.pita.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.devcold.pita.*
import ru.devcold.pita.databinding.ActivityInitialProfileBinding

class InitialProfileActivity : BaseActivity() {
    companion object {
        const val TAG = "InitialProfileActivity"
    }

    private val binding: ActivityInitialProfileBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_initial_profile)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding

        setUpViews()
    }

    private fun setUpViews() {
        binding.next.setOnClickListener {
            if(binding.userName.editText!!.text.isNullOrBlank()){
                binding.userName.isErrorEnabled = true
                binding.userName.error = resources.getString(R.string.enter_your_name)
            }else {
                writeName()
            }
        }
        binding.userName.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.userName.isErrorEnabled)
                    binding.userName.error = null
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.buttonSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun writeName() {
        val userId = getSP().getString(USER_ID, "")!!
        val userName = binding.userName.text
        val user = hashMapOf("name" to userName)
        lifecycleScope.launch {
            database.collection("users").document(userId)
                .set(user).await()
            Toast.makeText(this@InitialProfileActivity, user.toString(), Toast.LENGTH_LONG).show()
            getSPE().putString(USER_NAME, userName).apply()
            val intent = Intent(this@InitialProfileActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}