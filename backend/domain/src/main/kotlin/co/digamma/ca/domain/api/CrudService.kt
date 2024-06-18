package co.digamma.ca.domain.api

import co.digamma.ca.domain.api.model.Model

interface RetrieveService<T: Model> {

    fun retrieve(id: String): T
    fun retrieve(pageSpecs: PageSpecs): Page<T>
    fun retrieveAll(): List<T>
}

interface DeleteService {

    fun delete(id: String)
}