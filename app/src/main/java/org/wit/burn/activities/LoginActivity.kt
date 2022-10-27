package org.wit.burn.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.burn.R
import org.wit.burn.main.MainApp
import org.wit.burn.databinding.LoginViewBinding
import org.wit.burn.models.Login
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: LoginViewBinding
    var login = Login()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        Timber.i("Login Activity started...")

        binding.btnLogin.setOnClickListener() {
            login.username = binding.loginUsername.text.toString()
            login.password = binding.loginPassword.text.toString()

            if (login.username.isEmpty()) {
                Snackbar.make(it, R.string.enter_username, Snackbar.LENGTH_LONG)
                    .show()
            } else if (login.password.isEmpty()) {
                    Snackbar.make(it, R.string.enter_password, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    Snackbar.make(it, R.string.success, Snackbar.LENGTH_LONG)
                        .show()
                }
            Timber.i("Login Button Pressed")
        }
    }
}