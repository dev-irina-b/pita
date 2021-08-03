package ru.devcold.pita.login

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.devcold.pita.*
import ru.devcold.pita.databinding.ActivityWelcomeBinding

class WelcomeActivity : BaseActivity() {

    private lateinit var userId: String

    private val binding: ActivityWelcomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_welcome)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding

        userId = getSP().getString(USER_ID, "")!!

        if(userId.isBlank()){
            toast("userId is blank")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            checkDocExist()
        }
    }

    private fun checkDocExist() {
        lifecycleScope.launch {
            val doc = database.collection("users").document(userId).get().await()

            val intent = if(doc.exists() && doc.contains("name")) {
                getSPE().putString(USER_NAME, doc.getString("name")).apply()
                Intent(this@WelcomeActivity, MainActivity::class.java)
            } else {
                toast("doc not exist or doesn't contain name value")
                Intent(this@WelcomeActivity, InitialProfileActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}