package co.digamma.ca.domain.api

interface RetrieveService<T> {

    fun retrieve(id: String): T
    fun retrieve(pageSpecs: PageSpecs): Page<T>
    fun retrieveAll(): List<T>
}

interface CreateService<T> {

    fun create(model: T): T
}

interface UpdateService<T> {

    fun update(model: T): T
}

interface DeleteService {

    fun delete(id: String)
}

interface CrudService<T>:
    RetrieveService<T>, CreateService<T>, UpdateService<T>, DeleteService
