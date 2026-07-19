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
import co.digamma.ca.persistence.sql.instantFactory
import co.digamma.ca.persistence.sql.users.SqlUserRepository
import co.digamma.ca.suites.cookbook.QuantifiedIngredientRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlQuantifiedIngredientRepositoryTest(dsl: DSLContext) : QuantifiedIngredientRepositoryTestBase() {

    override val sut: QuantifiedIngredientRepository = SqlQuantifiedIngredientRepository(dsl, instantFactory)
    override val ingredientRepository: IngredientRepository = SqlIngredientRepository(dsl, instantFactory)
    override val measurementUnitRepository: MeasurementUnitRepository =
        SqlMeasurementUnitRepository(dsl, instantFactory)
    override val recipeRepository: RecipeRepository = SqlRecipeRepository(dsl, instantFactory)
    override val dishRepository = SqlDishRepository(dsl, instantFactory)
    override val courseRepository = SqlCourseRepository(dsl, instantFactory)
    override val cuisineRepository = SqlCuisineRepository(dsl, instantFactory)
    override val servingRepository = SqlServingRepository(dsl, instantFactory)
    override val userRepository = SqlUserRepository(dsl, instantFactory)
}
