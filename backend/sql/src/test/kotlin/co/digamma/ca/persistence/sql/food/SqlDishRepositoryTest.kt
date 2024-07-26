package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.persistence.sql.media.PostgreSQLContainerExtension
import co.digamma.ca.suites.food.DishRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlDishRepositoryTest(dsl: DSLContext) : DishRepositoryTestBase() {

    override val sut: DishRepository = SqlDishRepository(dsl)
    override val courseRepository = SqlCourseRepository(dsl)
    override val cuisineRepository = SqlCuisineRepository(dsl)
    override val servingRepository = SqlServingRepository(dsl)
}