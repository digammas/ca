package co.digamma.ca.persistence.sql

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.common.DuplicateKeyException
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UpdatableRecord
import org.springframework.beans.factory.ObjectFactory
import java.time.Instant
import java.util.logging.Logger
import kotlin.reflect.KClass

abstract class SqlCrudRepository<T : Model, R : UpdatableRecord<R>>(
    table: Table<R>,
    protected val idField: TableField<R, String?>,
    dsl: DSLContext,
    protected val modelType: KClass<T>,
    instantFactory: ObjectFactory<Instant>,
    log: Logger,
) : SqlRawRepository<R, String>(
    table = table,
    dsl = dsl,
    instantFactory = instantFactory,
    log = log,
), CrudRepository<T> {

    override fun idCondition(id: String): Condition = this.idField.eq(id)

    override fun retrieve(id: String): T? {
        return retrieveRecord(id)?.let(::toModel)
    }

    override fun retrieve(pageSpecs: PageSpecs): Page<T> = retrieveRecords(pageSpecs).map(::toModel)

    override fun retrieveAll(): List<T> = retrieveAllRecords().map(::toModel)

    override fun delete(id: String) = deleteRecord(id)

    override fun create(model: T): T {
        if (exists(model.id)) throw DuplicateKeyException("Model with ID ${model.id} already exists.")
        createRecord(toRecord(model))
        return model
    }

    override fun update(model: T): T {
        if (!exists(model.id)) throw NotFoundException("Model with ID ${model.id} not found.")
        updateRecord(this.toRecord(model))
        return model
    }

    protected fun exists(id: String): Boolean = recordExists(id)

    protected open fun toRecord(model: T): R {
        return this.dsl.newRecord(this.table, model)
    }

    protected open fun toModel(record: Record): T {
        return record.into(this.modelType.java)
    }
}