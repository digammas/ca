package co.digamma.ca.domain.internal

import co.digamma.ca.domain.api.CrudService
import co.digamma.ca.domain.api.common.Page
import co.digamma.ca.domain.api.common.PageSpecs
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.common.stereotypes.Transactional
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.api.model.ModelReference
import co.digamma.ca.domain.spi.CrudRepository
import co.digamma.ca.domain.spi.RetrieveRepository

@Transactional
abstract class DefaultCurdService<T : Model, C, M : ModelReference<T>> : CrudService<T, C, M> {

    protected abstract val repository: CrudRepository<T>

    protected val defaultPageSpecs get() = PageSpecs(0, 50)

    override fun retrieve(id: String): T {
        return this.repository.retrieve(id) ?: throw NotFoundException.of(id)
    }

    override fun retrieve(pageSpecs: PageSpecs?): Page<T> {
        return this.repository.retrieve(pageSpecs ?: this.defaultPageSpecs)
    }

    override fun retrieve(): List<T> {
        return this.repository.retrieveAll()
    }

    override fun create(creation: C): T {
        val model = this.toModel(creation)
        return this.repository.create(model)
    }

    override fun update(modification: M): T {
        val existing = this.retrieve(modification.id)
        val model = this.toModel(modification, existing)
        return this.repository.update(model)
    }

    override fun delete(id: String) {
        this.repository.delete(id)
    }

    protected fun generateId(): String {
        return java.util.UUID.randomUUID().toString()
    }

    abstract fun toModel(creation: C): T

    abstract fun toModel(modification: M, existing: T): T

    protected inline fun <reified T : Model> RetrieveRepository<T>.retrieveOrThrow(id: String): T {
        return this.retrieve(id) ?: throw NotFoundException.of(T::class.java, id)
    }
}
