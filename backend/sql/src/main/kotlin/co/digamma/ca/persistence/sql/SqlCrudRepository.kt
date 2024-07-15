package co.digamma.ca.persistence.sql

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.common.DuplicateKeyException
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.common.utils.Perhaps
import co.digamma.ca.domain.api.common.utils.asPerhaps
import co.digamma.ca.domain.api.model.Model
import co.digamma.ca.domain.spi.CrudRepository
import co.digamma.ca.persistence.jooq.media.tables.references.IMAGE
import java.time.LocalDateTime
import java.util.logging.Logger
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Table
import org.jooq.TableField
import org.jooq.UpdatableRecord
import org.jooq.impl.SQLDataType
import kotlin.reflect.KClass

private const val TIMESTAMP_FIELD_NAME = "timestamp"

abstract class SqlCrudRepository<T: Model, R: UpdatableRecord<R>>(
    protected val table: Table<R>,
    protected val idField: TableField<R, String?>,
    protected val dsl: DSLContext,
    protected val modelType: KClass<T>,
    protected val log: Logger
) : CrudRepository<T> {

    private var timestampField: Perhaps<Field<LocalDateTime>> = this.table.fields()
        .find { it.name.equals(TIMESTAMP_FIELD_NAME, true) && it.dataType.isTimestamp }
        ?.coerce(SQLDataType.LOCALDATETIME)
        .asPerhaps()

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
        if (exists(model.id)) throw DuplicateKeyException("Model with ID ${model.id} already exists.")
        val record = this.toRecord(model)
        this.timestamp(record)
        this.dsl.executeInsert(record)
        return model
    }

    override fun update(model: T): T {
        if (!exists(model.id)) throw NotFoundException("Model with ID ${model.id} not found.")
        val record = this.toRecord(model)
        this.timestamp(record)
        this.dsl.executeUpdate(record)
        return model
    }

    private fun exists(id: String): Boolean {
        return this.dsl.select(this.idField)
            .from(this.table)
            .where(this.idField.eq(id))
            .fetchOne() != null
    }

    protected open fun timestamp(record: R) {
        this.timestampField.ifPresent {
            record.set(it, LocalDateTime.now())
        }.orElse {
            // If the following message is being logged, consider overriding this method.
            this.log.warning { "Timestamp field not found for table ${this.table.name}." }
        }
    }

    protected open fun toRecord(model: T): R {
        return this.dsl.newRecord(this.table, model)
    }

    protected open fun toModel(record: R): T {
        return record.into(this.modelType.java)
    }
}