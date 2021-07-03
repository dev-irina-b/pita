package ru.devcold.pita

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import ru.devcold.pita.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_home)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding

        binding.buttonSignOut.setOnClickListener {
            signOut()
        }
    }
}
