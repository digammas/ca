package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.persistence.jooq.food.tables.records.ServingRecord
import co.digamma.ca.persistence.jooq.food.tables.references.SERVING
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.util.Locale
import java.util.logging.Logger
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

fun toServing(record: Record): Serving {
    val tempMin = record[SERVING.TEMP_MIN]
    val tempMax = record[SERVING.TEMP_MAX]
    val temperature = if (tempMin != null && tempMax != null) tempMin..tempMax else IntRange.EMPTY
    return Serving(
        id = record[SERVING.ID]!!,
        locale = Locale.of(record[SERVING.LOCALE]),
        name = record[SERVING.NAME]!!,
        temperature = temperature
    )
}

@Repository
class SqlServingRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()
) :
    SqlCrudRepository<Serving, ServingRecord>(SERVING, SERVING.ID, dsl, Serving::class, log),
    ServingRepository {

    override fun toModel(record: Record) = toServing(record)
}