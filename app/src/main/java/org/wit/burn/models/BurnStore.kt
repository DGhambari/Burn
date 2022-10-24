package org.wit.burn.models

interface BurnStore {
    fun findAll(): List<BurnModel>
    fun create(burn: BurnModel)
    fun update(burn: BurnModel)
}