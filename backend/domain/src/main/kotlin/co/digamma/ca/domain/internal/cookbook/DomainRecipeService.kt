package co.digamma.ca.domain.internal.cookbook

import co.digamma.ca.domain.api.common.Timestamp
import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredientCreation
import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.api.cookbook.RecipeCreation
import co.digamma.ca.domain.api.cookbook.RecipeModification
import co.digamma.ca.domain.api.cookbook.RecipeService
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.api.cookbook.StepCreation
import co.digamma.ca.domain.api.media.Images
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository
import co.digamma.ca.domain.spi.cookbook.QuantifiedIngredientRepository
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.cookbook.StepRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.media.ImageRepository
import co.digamma.ca.domain.spi.users.UserRepository

@Singleton
open class DomainRecipeService(
    override val repository: RecipeRepository,
    private val dishRepository: DishRepository,
    private val ingredientRepository: IngredientRepository,
    private val stepRepository: StepRepository,
    private val quantifiedIngredientRepository: QuantifiedIngredientRepository,
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository,
) : DefaultCurdService<Recipe, RecipeCreation, RecipeModification>(), RecipeService {

    override fun toModel(creation: RecipeCreation): Recipe {
        val now = Timestamp.now()
        return Recipe(
            id = generateId(),
            locale = creation.locale,
            dish = dishRepository.retrieveOrThrow(creation.dishId),
            yield = creation.yield,
            createdAt = now,
            updatedAt = now,
            author = userRepository.retrieveOrThrow(creation.author),
            images = Images(creation.imageIds.map { imageRepository.retrieveOrThrow(it) }),
            timeToServe = creation.timeToServe,
        )
    }

    override fun toModel(modification: RecipeModification, existing: Recipe) = Recipe(
        id = existing.id,
        locale = existing.locale,
        dish = modification.dishId?.let { dishRepository.retrieveOrThrow(it) } ?: existing.dish,
        yield = modification.yield ?: existing.yield,
        createdAt = existing.createdAt,
        updatedAt = existing.updatedAt,
        author = existing.author,
        images = modification.imageIds
            ?.map { imageRepository.retrieveOrThrow(it) }
            ?.let(::Images)
            ?: existing.images,
        timeToServe = modification.timeToServe ?: existing.timeToServe,
    )

    override fun create(creation: RecipeCreation): Recipe {
        val recipe = super.create(creation)

        creation.steps.map {
            it.toStep(recipe)
        }.forEach(stepRepository::create)
        creation.ingredients.map {
            it.toQuantifiedIngredient(recipe)
        }.forEach(quantifiedIngredientRepository::create)
        return recipe
    }

    private fun QuantifiedIngredientCreation.toQuantifiedIngredient(recipe: Recipe): QuantifiedIngredient {
        return QuantifiedIngredient(
            id = generateId(),
            recipe = recipe,
            ingredient = ingredientRepository.retrieveOrThrow(this.ingredientId),
            quantity = this.quantity,
            unit = measurementUnitRepository.retrieveOrThrow(this.measurementUnitId),
        )
    }

    private fun StepCreation.toStep(recipe: Recipe): Step {
        return Step(
            id = generateId(),
            recipe = recipe,
            locale = recipe.locale,
            description = this.description,
            estimatedTime = this.estimatedTime,
        )
    }
}

