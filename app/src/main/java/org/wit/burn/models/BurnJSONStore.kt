package org.wit.burn.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.burn.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "burns.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<BurnModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class BurnJSONStore(private val context: Context) : BurnStore {

    var burns = mutableListOf<BurnModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<BurnModel> {
        logAll()
        return burns
    }

    override fun create(burn: BurnModel) {
        burn.id = generateRandomId()
        burns.add(burn)
        serialize()
    }

    override fun update(burn: BurnModel) {
        val burnsList = findAll() as ArrayList<BurnModel>
        var foundBurn: BurnModel? = burnsList.find { p -> p.id == burn.id }
        if (foundBurn != null) {
            foundBurn.title = burn.title
            foundBurn.description = burn.description
            foundBurn.image = burn.image
            foundBurn.lat = burn.lat
            foundBurn.lng = burn.lng
            foundBurn.zoom = burn.zoom
        }
        serialize()
    }

    override fun findById(id: Long): BurnModel? {
        val foundBurn: BurnModel? = burns.find { it.id == id }
        return foundBurn
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(burns, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        burns = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        burns.forEach { Timber.i("$it") }
    }

    override fun delete(burn: BurnModel) {
        burns.remove(burn)
        serialize()
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}