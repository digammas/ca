package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.persistence.jooq.food.tables.records.ServingRecord
import co.digamma.ca.persistence.jooq.food.tables.references.SERVING
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.util.logging.Logger
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class SqlServingRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()):
    SqlCrudRepository<Serving, ServingRecord>(SERVING, SERVING.ID, dsl, Serving::class, log),
    ServingRepository