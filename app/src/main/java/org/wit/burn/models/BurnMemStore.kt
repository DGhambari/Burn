package org.wit.burn.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class BurnMemStore : BurnStore {

    val burns = ArrayList<BurnModel>()

    override fun findAll(): List<BurnModel> {
        return burns
    }

    override fun create(burn: BurnModel) {
        burn.id = getId()
        burns.add(burn)
        logAll()
    }

    override fun update(burn: BurnModel) {
        var foundBurn: BurnModel? = burns.find { p -> p.id == burn.id }
        if (foundBurn != null) {
            foundBurn.title = burn.title
            foundBurn.description = burn.description
            foundBurn.image = burn.image
            foundBurn.lat = burn.lat
            foundBurn.lng = burn.lng
            foundBurn.zoom = burn.zoom
            logAll()
        }
    }

    private fun logAll() {
        burns.forEach { i("$it") }
    }
}