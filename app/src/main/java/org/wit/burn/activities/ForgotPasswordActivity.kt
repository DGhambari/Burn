package org.wit.burn.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.wit.burn.R
import org.wit.burn.databinding.ForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        binding = ForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textBack.setOnClickListener(){
//            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
            onBackPressed()
        }
    }
}