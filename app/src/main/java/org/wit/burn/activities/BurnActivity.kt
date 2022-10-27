package org.wit.burn.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.burn.helpers.showImagePicker
import org.wit.burn.main.MainApp
import org.wit.burn.models.Location
import org.wit.burn.models.BurnModel
import org.wit.burn.R
import org.wit.burn.databinding.ActivityBurnBinding
import timber.log.Timber.i

class BurnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBurnBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var location = Location(52.245696, -7.139102, 15f)
    var burn = BurnModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false
        var delete = false
        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        binding = ActivityBurnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Burn Activity started...")

        if (intent.hasExtra("burn_edit")) {
            edit = true
            burn = intent.extras?.getParcelable("burn_edit")!!
            binding.burnTitle.setText(burn.title)
            binding.description.setText(burn.description)
//            binding.latitude.setText(burn.latitude)
//            binding.longitude.setText(burn.longitude)
            binding.btnAdd.setText(R.string.save_location)
            Picasso.get()
                .load(burn.image)
                .into(binding.burnImage)
            if (burn.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_route_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            burn.title = binding.burnTitle.text.toString()
            burn.description = binding.description.text.toString()
            if (burn.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_route_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.burns.update(burn.copy())
                } else {
                    app.burns.create(burn.copy())
                }
            }
            i("add Button Pressed: $burn")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }

        binding.burnLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_burn, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            burn.image = result.data!!.data!!
                            Picasso.get()
                                .load(burn.image)
                                .into(binding.burnImage)
                            binding.chooseImage.setText(R.string.change_route_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}