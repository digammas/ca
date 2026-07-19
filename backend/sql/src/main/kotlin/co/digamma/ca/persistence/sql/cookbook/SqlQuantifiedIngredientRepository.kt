package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.spi.cookbook.QuantifiedIngredientRepository
import co.digamma.ca.persistence.jooq.cookbook.tables.records.QuantifiedIngredientRecord
import co.digamma.ca.persistence.jooq.cookbook.tables.references.QUANTIFIED_INGREDIENT
import co.digamma.ca.persistence.sql.SqlCrudRepository
import org.jooq.DSLContext
import org.jooq.Path
import org.jooq.Record
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.logging.Logger

fun toQuantifiedIngredient(record: Record): QuantifiedIngredient {
    val ingredient = toIngredient(record)
    val unit = toMeasurementUnit(record)
    val recipe = toRecipe(record)

    return QuantifiedIngredient(
        id = record[QUANTIFIED_INGREDIENT.ID]!!,
        ingredient = ingredient,
        quantity = record[QUANTIFIED_INGREDIENT.QUANTITY]!!,
        unit = unit,
        recipe = recipe,
    )
}

@Repository
open class SqlQuantifiedIngredientRepository(
    dsl: DSLContext,
    instantFactory: ObjectFactory<Instant>,
    log: Logger = LoggerFactory.forClass(),
) :
    SqlCrudRepository<QuantifiedIngredient, QuantifiedIngredientRecord>(
        QUANTIFIED_INGREDIENT,
        QUANTIFIED_INGREDIENT.ID,
        dsl,
        QuantifiedIngredient::class,
        instantFactory,
        log,
    ),
    QuantifiedIngredientRepository {

    override val joinPaths: List<Path<*>> = listOf(
        QUANTIFIED_INGREDIENT.ingredient,
        QUANTIFIED_INGREDIENT.measurementUnit,
        QUANTIFIED_INGREDIENT.recipe,
        QUANTIFIED_INGREDIENT.recipe.dish,
        QUANTIFIED_INGREDIENT.recipe.dish.course,
        QUANTIFIED_INGREDIENT.recipe.dish.cuisine,
        QUANTIFIED_INGREDIENT.recipe.dish.serving,
        QUANTIFIED_INGREDIENT.recipe.userAccount,
    )

    override fun toRecord(model: QuantifiedIngredient) = super.toRecord(model).also {
        it.ingredientId = model.ingredient.id
        it.unitId = model.unit.id
        it.recipeId = model.recipe.id
    }

    override fun toModel(record: Record) = toQuantifiedIngredient(record)

    override fun retrieveByRecipe(recipeId: String, pageSpecs: PageSpecs): Page<QuantifiedIngredient> {
        val total = this.dsl.selectCount().from(QUANTIFIED_INGREDIENT)
            .where(QUANTIFIED_INGREDIENT.RECIPE_ID.eq(recipeId))
            .fetchOne(0, Int::class.java)!!
        val list = this.dsl.select().from(this.joinedTable)
            .where(QUANTIFIED_INGREDIENT.RECIPE_ID.eq(recipeId))
            .offset(pageSpecs.index * pageSpecs.size)
            .limit(pageSpecs.size)
            .fetch(this::toModel)
        return Page(list, pageSpecs.index, pageSpecs.size, total)
    }
}
