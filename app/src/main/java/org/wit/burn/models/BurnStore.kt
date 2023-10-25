package org.wit.burn.models

interface BurnStore {
    fun findAll(): List<BurnModel>
    fun findById(id:Long) : BurnModel?
    fun create(burn: BurnModel)
    fun update(burn: BurnModel)
    fun delete(burn: BurnModel)
}