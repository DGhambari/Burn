package burn.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BurnModel(var id: Long = 0,
                          var title: String = "",
                          var image: Uri = Uri.EMPTY,
                          var description: String = "",
                          var latitude: Double = 0.0,
                          var longitude: Double = 0.0) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable