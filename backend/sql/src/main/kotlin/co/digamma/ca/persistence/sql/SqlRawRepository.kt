package co.digamma.ca.persistence.sql

import co.digamma.ca.domain.api.common.Page
import co.digamma.ca.domain.api.common.PageSpecs
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Path
import org.jooq.Record
import org.jooq.Table
import org.jooq.UpdatableRecord
import org.jooq.impl.SQLDataType
import org.springframework.beans.factory.ObjectFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.logging.Logger

private const val TIMESTAMP_FIELD_NAME = "timestamp"

abstract class SqlRawRepository<R : UpdatableRecord<R>, I>(
    protected val table: Table<R>,
    protected val dsl: DSLContext,
    protected val instantFactory: ObjectFactory<Instant>,
    protected val log: Logger,
) {

    protected abstract fun idCondition(id: I): Condition

    private var timestampField: Field<LocalDateTime>? = this.table.fields()
        .find { it.name.equals(TIMESTAMP_FIELD_NAME, true) && it.dataType.isTimestamp }
        ?.coerce(SQLDataType.LOCALDATETIME)

    open val joinPaths: List<Path<*>> = emptyList()

    protected val joinedTable get(): Table<*> = this.joinPaths.fold(this.table as Table<*>) { acc, value ->
        acc.join(value)
    }

    open fun retrieveRecord(id: I): Record? {
        return this.dsl.select().from(this.joinedTable)
            .where(idCondition(id))
            .fetchOne()
    }

    open fun retrieveRecords(pageSpecs: PageSpecs): Page<Record> {
        val total = this.dsl.selectCount().from(this.table).fetchOne(0, Int::class.java)!!
        val list = this.dsl.select().from(this.joinedTable)
            .offset(pageSpecs.index * pageSpecs.size)
            .limit(pageSpecs.size)
            .fetch()
        return Page(list, pageSpecs.index, pageSpecs.size, total)
    }

    open fun retrieveAllRecords(): List<Record> {
        return this.dsl.select().from(this.joinedTable)
            .fetch()
    }

    open fun deleteRecord(id: I) {
        this.dsl.deleteFrom(this.table)
            .where(idCondition(id))
            .execute()
    }

    open fun createRecord(record: R): R {
        this.timestamp(record)
        this.dsl.executeInsert(record)
        return record
    }

    open fun updateRecord(record: R): R {
        this.timestamp(record)
        this.dsl.executeUpdate(record)
        return record
    }

    protected fun recordExists(id: I): Boolean {
        return this.dsl.select()
            .from(this.table)
            .where(idCondition(id))
            .fetchOne() != null
    }

    protected fun now(): LocalDateTime = LocalDateTime.ofInstant(instantFactory.getObject(), ZoneId.systemDefault())

    protected open fun timestamp(record: R) {
        this.timestampField?.also {
            record.set(it, now())
        } ?: run {
            // If the following message is being logged, consider overriding this method.
            this.log.warning { "Timestamp field not found for table ${this.table.name}." }
        }
    }
}