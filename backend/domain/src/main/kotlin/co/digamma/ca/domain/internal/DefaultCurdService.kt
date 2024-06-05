package co.digamma.ca.domain.internal

import co.digamma.ca.domain.api.CrudService
import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository

abstract class DefaultCurdService<T: Model>: CrudService<T> {

    abstract val repository: CrudRepository<T>

    override fun retrieve(id: String): T {
        return this.repository.retrieve(id)
    }

    override fun retrieve(pageSpecs: PageSpecs): Page<T> {
        return this.repository.retrieve(pageSpecs)
    }

    override fun retrieveAll(): List<T> {
        return this.repository.retrieveAll()
    }

    override fun delete(id: String) {
        this.repository.delete(id)
    }

    override fun update(model: T): T {
        return this.repository.update(model)
    }

    override fun create(model: T): T {
        return this.repository.create(model)
    }
}