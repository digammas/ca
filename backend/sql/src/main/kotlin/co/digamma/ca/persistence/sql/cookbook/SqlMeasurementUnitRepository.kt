package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.cookbook.MeasurementUnit
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository
import co.digamma.ca.persistence.jooq.cookbook.tables.records.MeasurementUnitRecord
import co.digamma.ca.persistence.jooq.cookbook.tables.references.MEASUREMENT_UNIT
import co.digamma.ca.persistence.sql.SqlCrudRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.logging.Logger

fun toMeasurementUnit(record: Record): MeasurementUnit {
    val dimensionStr = record[MEASUREMENT_UNIT.DIMENSION]!!
    val dimension = MeasurementUnit.Dimension.valueOf(dimensionStr)
    return MeasurementUnit(
        id = record[MEASUREMENT_UNIT.ID]!!,
        locale = record[MEASUREMENT_UNIT.LOCALE]!!,
        name = record[MEASUREMENT_UNIT.NAME]!!,
        dimension = dimension,
    )
}

@Repository
open class SqlMeasurementUnitRepository(
    dsl: DSLContext,
    instantFactory: ObjectFactory<Instant>,
    log: Logger = LoggerFactory.forClass(),
) :
    SqlCrudRepository<MeasurementUnit, MeasurementUnitRecord>(
        MEASUREMENT_UNIT,
        MEASUREMENT_UNIT.ID,
        dsl,
        MeasurementUnit::class,
        instantFactory,
        log,
    ),
    MeasurementUnitRepository {

    override fun toModel(record: Record) = toMeasurementUnit(record)
}
