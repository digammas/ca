package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.domain.api.common.Timestamp
import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.api.media.noImages
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.persistence.jooq.cookbook.tables.records.RecipeRecord
import co.digamma.ca.persistence.jooq.cookbook.tables.references.RECIPE
import co.digamma.ca.persistence.sql.SqlCrudRepository
import co.digamma.ca.persistence.sql.food.toDish
import co.digamma.ca.persistence.sql.users.toUser
import org.jooq.DSLContext
import org.jooq.Path
import org.jooq.Record
import org.springframework.stereotype.Repository
import java.util.logging.Logger

fun toRecipe(record: Record): Recipe {
    val dish = toDish(record)
    val createdAt = Timestamp.of(record[RECIPE.CREATED_AT]!!)
    val updatedAt = Timestamp.of(record[RECIPE.UPDATED_AT]!!)
    val author = toUser(record)
    return Recipe(
        id = record[RECIPE.ID]!!,
        locale = record[RECIPE.LOCALE]!!,
        dish = dish,
        ingredients = emptyList(),
        steps = emptyList(),
        yield = record[RECIPE.YIELD]!!,
        createdAt = createdAt,
        updatedAt = updatedAt,
        author = author,
        images = noImages(),
        timeToServe = record[RECIPE.TIME_TO_SERVE],
    )
}

@Repository
open class SqlRecipeRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()
) :
    SqlCrudRepository<Recipe, RecipeRecord>(RECIPE, RECIPE.ID, dsl, Recipe::class, log),
    RecipeRepository {

    override val joinPaths: List<Path<*>> = listOf(
        RECIPE.userAccount,
        RECIPE.dish,
        RECIPE.dish.course,
        RECIPE.dish.cuisine,
        RECIPE.dish.serving,
    )

    override fun toRecord(model: Recipe) = super.toRecord(model).also {
        it.dishId = model.dish.id
        it.authorId = model.author.username
    }

    override fun toModel(record: Record) = toRecipe(record)
}
