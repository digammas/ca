package co.digamma.ca.persistence.sql.food

import co.digamma.ca.persistence.sql.media.PostgreSQLContainerExtension
import co.digamma.ca.suites.food.ServingRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlServingRepositoryTest(dsl: DSLContext) : ServingRepositoryTestBase() {
    override val sut = SqlServingRepository(dsl)
}