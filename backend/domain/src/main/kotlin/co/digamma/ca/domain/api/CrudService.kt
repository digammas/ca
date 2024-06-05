package co.digamma.ca.domain.api

import co.digamma.ca.domain.api.model.Model

interface RetrieveService<T: Model> {

    fun retrieve(id: String): T
    fun retrieve(pageSpecs: PageSpecs): Page<T>
    fun retrieveAll(): List<T>
}

interface CreateService<T: Model> {

    fun create(model: T): T
}

interface UpdateService<T: Model> {

    fun update(model: T): T
}

interface DeleteService {

    fun delete(id: String)
}

interface CrudService<T: Model>:
    RetrieveService<T>, CreateService<T>, UpdateService<T>, DeleteService
