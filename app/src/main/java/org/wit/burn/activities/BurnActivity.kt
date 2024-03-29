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
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import org.wit.burn.helpers.showImagePicker
import org.wit.burn.main.MainApp
import org.wit.burn.models.Location
import org.wit.burn.models.BurnModel
import org.wit.burn.R
import org.wit.burn.databinding.ActivityBurnBinding
import android.view.View
import timber.log.Timber.i

class BurnActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBurnBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
//    var location = Location(52.245696, -7.139102, 15f)
    var burn = BurnModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityBurnBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.toolbarAdd.title = title
//        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Burn Activity started...")

        if (intent.hasExtra("burn_edit")) {
            edit = true
            burn = intent.extras?.getParcelable("burn_edit")!!
            binding.routeName.setText(burn.title)
            binding.description.setText(burn.description)
            binding.btnAdd.setText(R.string.save_location)
            binding.btnDelete.visibility = View.VISIBLE
            Picasso.get()
                .load(burn.image)
                .into(binding.routeImage)
            if (burn.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_route_image)
            }
        }

        binding.btnDelete.setOnClickListener {
            deleteBurn()
        }

        binding.btnAdd.setOnClickListener() {
            burn.title = binding.routeName.text.toString()
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

            val intent = Intent(this@BurnActivity, BurnListActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@BurnActivity, LoginActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            i("Select image")
            showImagePicker(imageIntentLauncher)
        }

        binding.routeLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (burn.zoom !=0f) {
                location.lat = burn.lat
                location.lng = burn.lng
                location.zoom = burn.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.btnDelete -> {
                deleteBurn()
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
                                .into(binding.routeImage)
                            binding.chooseImage.setText(R.string.change_route_image)
                        }
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
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            burn.lat = location.lat
                            burn.lng = location.lng
                            burn.zoom =location.zoom
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun deleteBurn() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Delete Route")
            .setMessage("Are you sure you want to delete this route?")
            .setPositiveButton("Yes") { _, _ ->
                app.burns.delete(burn)
                setResult(RESULT_OK)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}