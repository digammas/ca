package co.digamma.ca.persistence.sql.food

import co.digamma.ca.persistence.sql.media.PostgreSQLContainerExtension
import co.digamma.ca.suites.food.CuisineRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlCuisineRepositoryTest(dsl: DSLContext) : CuisineRepositoryTestBase() {
    override val sut = SqlCuisineRepository(dsl)
}