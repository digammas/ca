package co.digamma.ca.persistence.sql

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository
import co.digamma.ca.persistence.jooq.media.tables.references.IMAGE
import org.jooq.Record
import org.jooq.Table
import org.jooq.TableField
import kotlin.reflect.KClass

abstract class SqlCrudRepository<T: Model, R: Record>(
    protected val table: Table<R>,
    protected val idField: TableField<R, String?>,
    protected val dsl: org.jooq.DSLContext,
    protected val modelType: KClass<T>
) : CrudRepository<T> {

    override fun retrieve(id: String): T? {
        return dsl.selectFrom(this.table)
            .where(this.idField.eq(id))
            .fetchOne(this::toModel)
    }

    override fun retrieve(pageSpecs: PageSpecs): Page<T> {
        val total = dsl.selectCount().from(IMAGE).fetchOne(0, Int::class.java)!!
        val list =  dsl.selectFrom(this.table)
            .offset(pageSpecs.index * pageSpecs.size)
            .limit(pageSpecs.size)
            .fetch(this::toModel)
        return Page(list, pageSpecs.index, pageSpecs.size, total)
    }

    override fun retrieveAll(): List<T> {
        return dsl.selectFrom(this.table)
            .fetch(this::toModel)
    }

    override fun delete(id: String) {
        dsl.deleteFrom(this.table)
            .where(this.idField.eq(id))
            .execute()
    }

    protected fun toModel(record: R): T {
        return record.into(this.modelType.java)
    }
}