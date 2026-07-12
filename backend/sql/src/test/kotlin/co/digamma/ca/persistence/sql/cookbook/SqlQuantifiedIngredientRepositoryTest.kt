package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository
import co.digamma.ca.domain.spi.cookbook.QuantifiedIngredientRepository
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.persistence.sql.food.SqlCourseRepository
import co.digamma.ca.persistence.sql.food.SqlCuisineRepository
import co.digamma.ca.persistence.sql.food.SqlDishRepository
import co.digamma.ca.persistence.sql.food.SqlServingRepository
import co.digamma.ca.persistence.sql.PostgreSQLContainerExtension
import co.digamma.ca.persistence.sql.users.SqlUserRepository
import co.digamma.ca.suites.cookbook.QuantifiedIngredientRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlQuantifiedIngredientRepositoryTest(dsl: DSLContext) : QuantifiedIngredientRepositoryTestBase() {

    override val sut: QuantifiedIngredientRepository = SqlQuantifiedIngredientRepository(dsl)
    override val ingredientRepository: IngredientRepository = SqlIngredientRepository(dsl)
    override val measurementUnitRepository: MeasurementUnitRepository = SqlMeasurementUnitRepository(dsl)
    override val recipeRepository: RecipeRepository = SqlRecipeRepository(dsl)
    override val dishRepository = SqlDishRepository(dsl)
    override val courseRepository = SqlCourseRepository(dsl)
    override val cuisineRepository = SqlCuisineRepository(dsl)
    override val servingRepository = SqlServingRepository(dsl)
    override val userRepository = SqlUserRepository(dsl)
}
