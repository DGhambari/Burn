package org.wit.burn.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.burn.R
import org.wit.burn.databinding.LoginViewBinding
import org.wit.burn.main.MainApp
import org.wit.burn.models.Login
import org.wit.burn.models.Register
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

        binding.forgotPassword.setOnClickListener() {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

        binding.register.setOnClickListener() {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener() {
            login.username = binding.loginUsername.text.toString()
            login.password = binding.loginPassword.text.toString()

            when {
                login.username.isEmpty() -> {
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.enter_username,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                login.password.isEmpty() -> {
                    Snackbar.make(it, R.string.enter_password, Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> {

                    val email: String = login.username
                    val password: String = login.password

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {

                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this@LoginActivity,
                                        R.string.success,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent =
                                        Intent(this@LoginActivity, BurnActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra(
                                        "user_id",
                                        FirebaseAuth.getInstance().currentUser!!.uid
                                    )
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()

                                } else {
                                    // If the login is not successful then show an error message.
                                    Toast.makeText(
                                        this@LoginActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                }
            }
        }
    }
}