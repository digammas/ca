package co.digamma.ca.domain.api

import co.digamma.ca.domain.api.model.Model

interface RetrieveService<T : Model> {
    fun retrieve(id: String): T
    fun retrieve(pageSpecs: PageSpecs): Page<T>
    fun retrieve(): List<T>
}

interface CreateService<T : Model, C> {
    fun create(creation: C): T
}

interface UpdateService<T : Model, M> {
    fun update(modification: M): T
}

interface DeleteService {
    fun delete(id: String)
}

interface CrudService<T : Model, C, M> :
    RetrieveService<T>,
    CreateService<T, C>,
    UpdateService<T, M>,
    DeleteService