package co.digamma.ca.domain.internal

import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.RetrieveService
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository
import co.digamma.ca.domain.spi.RetrieveRepository

abstract class DefaultCurdService<T : Model> : RetrieveService<T>, DeleteService {

    protected abstract val repository: CrudRepository<T>

    override fun retrieve(id: String): T {
        return this.repository.retrieve(id) ?: throw NotFoundException.of(id)
    }

    override fun retrieve(pageSpecs: PageSpecs): Page<T> {
        return this.repository.retrieve(pageSpecs)
    }

    override fun retrieve(): List<T> {
        return this.repository.retrieveAll()
    }

    override fun delete(id: String) {
        this.repository.delete(id)
    }

    fun generateId(): String {
        return java.util.UUID.randomUUID().toString()
    }
}

inline fun <reified T : Model> RetrieveRepository<T>.retrieveOrThrow(id: String): T {
    return this.retrieve(id) ?: throw NotFoundException.of(T::class.java, id)
}