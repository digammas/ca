package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.cookbook.StepRepository
import co.digamma.ca.persistence.sql.PostgreSQLContainerExtension
import co.digamma.ca.persistence.sql.instantFactory
import co.digamma.ca.persistence.sql.food.SqlCourseRepository
import co.digamma.ca.persistence.sql.food.SqlCuisineRepository
import co.digamma.ca.persistence.sql.food.SqlDishRepository
import co.digamma.ca.persistence.sql.food.SqlServingRepository
import co.digamma.ca.persistence.sql.users.SqlUserRepository
import co.digamma.ca.suites.cookbook.StepRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlStepRepositoryTest(dsl: DSLContext) : StepRepositoryTestBase() {

    override val sut: StepRepository = SqlStepRepository(dsl, instantFactory)
    override val recipeRepository: RecipeRepository = SqlRecipeRepository(dsl, instantFactory)
    override val dishRepository = SqlDishRepository(dsl, instantFactory)
    override val courseRepository = SqlCourseRepository(dsl, instantFactory)
    override val cuisineRepository = SqlCuisineRepository(dsl, instantFactory)
    override val servingRepository = SqlServingRepository(dsl, instantFactory)
    override val userRepository = SqlUserRepository(dsl, instantFactory)
}
