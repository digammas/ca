package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.persistence.sql.PostgreSQLContainerExtension
import co.digamma.ca.persistence.sql.instantFactory
import co.digamma.ca.suites.cookbook.MeasurementUnitRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlMeasurementUnitRepositoryTest(dsl: DSLContext) : MeasurementUnitRepositoryTestBase() {

    override val sut = SqlMeasurementUnitRepository(dsl, instantFactory)
}