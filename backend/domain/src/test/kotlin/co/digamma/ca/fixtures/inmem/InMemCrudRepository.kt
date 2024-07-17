package co.digamma.ca.fixtures.inmem

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.common.DuplicateKeyException
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository

open class InMemCrudRepository<T: Model>: CrudRepository<T> {
    private val data: MutableMap<String, T> = HashMap()

    override fun retrieve(id: String): T? = data[id]

    override fun retrieve(pageSpecs: PageSpecs): Page<T> {
        val (index, size) = pageSpecs
        val list = data.values.stream()
            .skip(index * size.toLong())
            .limit(size.toLong())
            .toList()
        return Page(list, index, data.size)
    }

    override fun retrieveAll(): List<T> = data.values.toList()

    @Synchronized
    override fun create(model: T): T {
        if (data.containsKey(model.id)) {
            throw DuplicateKeyException.of(model.javaClass, model.id)
        }
        data[model.id] = model
        return model
    }

    override fun update(model: T): T {
        if (!data.containsKey(model.id)) {
            throw NotFoundException.of(model.javaClass, model.id)
        }
        data[model.id] = model
        return model
    }

    override fun delete(id: String) {
        if (!data.containsKey(id)) {
            throw NotFoundException.of(id)
        }
        data.remove(id)
    }
}
