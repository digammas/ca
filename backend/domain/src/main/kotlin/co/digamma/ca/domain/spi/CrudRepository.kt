package co.digamma.ca.domain.spi

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.model.Model

interface RetrieveRepository<T: Model> {

    fun retrieve(id: String): T?
    fun retrieve(pageSpecs: PageSpecs): Page<T>
    fun retrieveAll(): List<T>
}

interface CreateRepository<T: Model> {

    fun create(model: T): T
}

interface UpdateRepository<T: Model> {

    fun update(model: T): T
}

interface DeleteRepository {

    fun delete(id: String)
}

interface CrudRepository<T: Model>:
    RetrieveRepository<T>, CreateRepository<T>, UpdateRepository<T>, DeleteRepository
