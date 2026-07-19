package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.persistence.sql.PostgreSQLContainerExtension
import co.digamma.ca.persistence.sql.instantFactory
import co.digamma.ca.suites.food.DishRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlDishRepositoryTest(dsl: DSLContext) : DishRepositoryTestBase() {

    override val sut: DishRepository = SqlDishRepository(dsl, instantFactory)
    override val courseRepository = SqlCourseRepository(dsl, instantFactory)
    override val cuisineRepository = SqlCuisineRepository(dsl, instantFactory)
    override val servingRepository = SqlServingRepository(dsl, instantFactory)
}