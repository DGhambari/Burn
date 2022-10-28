package org.wit.burn.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.burn.R
import org.wit.burn.databinding.ActivityRegisterBinding
import org.wit.burn.main.MainApp

import org.wit.burn.models.Register
import timber.log.Timber

class RegisterActivity : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivityRegisterBinding
    var register = Register()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        Timber.i("Register Activity started...")

        binding.textRegisterBack.setOnClickListener(){
//            startActivity(Intent(this@RegisterActivity, ForgotPasswordActivity::class.java))
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener() {
            register.username = binding.registerUsername.text.toString()
            register.password = binding.registerPassword.text.toString()

            when {
                register.username.isEmpty() -> {
                    Toast.makeText(this@RegisterActivity,
                        R.string.enter_username,
                        Toast.LENGTH_SHORT)
                        .show()
                }
                register.password.isEmpty() -> {
                    Toast.makeText(this@RegisterActivity, R.string.enter_password, Toast.LENGTH_SHORT)
                        .show()
                } else -> {

                val email: String = register.username
                val password: String = register.password

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult>{ task ->
                            if (task.isSuccessful){

                             val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@RegisterActivity,
                                    R.string.success,
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@RegisterActivity, BurnActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                Toast.makeText(this@RegisterActivity, "Success!",Toast.LENGTH_SHORT)
                                startActivity(intent)
                                finish()
                            } else {
                                // If the registering is not successful then show an error message.
                                Toast.makeText(this@RegisterActivity,
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
