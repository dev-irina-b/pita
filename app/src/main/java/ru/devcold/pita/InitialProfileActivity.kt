package ru.devcold.pita

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ru.devcold.pita.databinding.ActivityInitialProfileBinding

class InitialProfileActivity : AppCompatActivity() {

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
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("userName", binding.userName.editText!!.text.toString())
                startActivity(intent)
                finish()
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
    }

    override fun onBackPressed() {
        Toast.makeText(this, "onBackPressed", Toast.LENGTH_SHORT).show()
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}