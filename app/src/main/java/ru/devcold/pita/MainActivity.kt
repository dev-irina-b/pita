package ru.devcold.pita

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import ru.devcold.pita.databinding.ActivityHomeBinding

class MainActivity : BaseActivity() {

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        setUpViews()
    }
    
    private fun setUpViews() {
        binding.buttonSignOut.setOnClickListener {
            signOut()
        }

        val userId = getSP().getString(USER_ID, "")!!
        val userName = getSP().getString(USER_NAME, "5")!!
        val greetingText = resources.getString(R.string.hello_user, userName)
        binding.greeting.text = greetingText

        binding.progress.visibility = View.GONE
        binding.greeting.visibility = View.VISIBLE
        Toast.makeText(this, userId, Toast.LENGTH_LONG).show()
    }
}
