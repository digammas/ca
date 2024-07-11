package co.digamma.ca.persistence.sql

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository
import co.digamma.ca.persistence.jooq.media.tables.references.IMAGE
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UpdatableRecord
import kotlin.reflect.KClass

abstract class SqlCrudRepository<T: Model, R: UpdatableRecord<R>>(
    protected val table: Table<R>,
    protected val idField: TableField<R, String?>,
    protected val dsl: org.jooq.DSLContext,
    protected val modelType: KClass<T>
) : CrudRepository<T> {

    override fun retrieve(id: String): T? {
        return this.dsl.selectFrom(this.table)
            .where(this.idField.eq(id))
            .fetchOne(this::toModel)
    }

    override fun retrieve(pageSpecs: PageSpecs): Page<T> {
        val total = this.dsl.selectCount().from(IMAGE).fetchOne(0, Int::class.java)!!
        val list =  this.dsl.selectFrom(this.table)
            .offset(pageSpecs.index * pageSpecs.size)
            .limit(pageSpecs.size)
            .fetch(this::toModel)
        return Page(list, pageSpecs.index, pageSpecs.size, total)
    }

    override fun retrieveAll(): List<T> {
        return this.dsl.selectFrom(this.table)
            .fetch(this::toModel)
    }

    override fun delete(id: String) {
        this.dsl.deleteFrom(this.table)
            .where(this.idField.eq(id))
            .execute()
    }

    override fun create(model: T): T {
        val record = this.toRecord(model)
        this.dsl.executeInsert(record)
        return model
    }

    override fun update(model: T): T {
        val record = this.toRecord(model)
        this.dsl.executeUpdate(record)
        return model
    }

    protected fun toRecord(model: T): R {
        return this.dsl.newRecord(this.table, model)
    }

    protected fun toModel(record: R): T {
        return record.into(this.modelType.java)
    }
}