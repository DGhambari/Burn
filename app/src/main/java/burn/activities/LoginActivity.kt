package burn.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import burn.main.MainApp
import org.wit.burn.R
import org.wit.burn.databinding.ActivityBurnBinding
import org.wit.burn.databinding.ActivityBurnListBinding
import org.wit.burn.databinding.LoginViewBinding
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: LoginViewBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.toolbarAdd.title = title
//        setSupportActionBar(binding.toolbarAdd)



        app = application as MainApp

        Timber.i("Burn Activity started...")

    }

}