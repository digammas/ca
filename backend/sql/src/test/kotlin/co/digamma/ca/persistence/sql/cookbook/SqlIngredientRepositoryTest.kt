package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.persistence.sql.PostgreSQLContainerExtension
import co.digamma.ca.suites.cookbook.IngredientRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlIngredientRepositoryTest(dsl: DSLContext) : IngredientRepositoryTestBase() {

    override val sut = SqlIngredientRepository(dsl)
}