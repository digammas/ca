package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.persistence.jooq.food.tables.records.CuisineRecord
import co.digamma.ca.persistence.jooq.food.tables.references.CUISINE
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.util.Locale
import java.util.logging.Logger
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

fun toCuisine(record: Record): Cuisine {
    return Cuisine(
        id = record[CUISINE.ID]!!,
        locale = Locale.of(record[CUISINE.LOCALE]),
        name = record[CUISINE.NAME]!!,
    )
}

@Repository
open class SqlCuisineRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()
) :
    SqlCrudRepository<Cuisine, CuisineRecord>(CUISINE, CUISINE.ID, dsl, Cuisine::class, log),
    CuisineRepository {

    override fun toModel(record: Record) = toCuisine(record)
}
