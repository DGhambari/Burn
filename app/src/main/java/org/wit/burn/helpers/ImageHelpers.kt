package org.wit.burn.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import org.wit.burn.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_route_image.toString())
    intentLauncher.launch(chooseFile)
}