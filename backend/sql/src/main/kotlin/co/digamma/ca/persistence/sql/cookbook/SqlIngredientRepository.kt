package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.cookbook.Ingredient
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.persistence.jooq.cookbook.tables.records.IngredientRecord
import co.digamma.ca.persistence.jooq.cookbook.tables.references.INGREDIENT
import co.digamma.ca.persistence.sql.SqlCrudRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.logging.Logger

fun toIngredient(record: Record): Ingredient {
    return Ingredient(
        id = record[INGREDIENT.ID]!!,
        locale = record[INGREDIENT.LOCALE]!!,
        name = record[INGREDIENT.NAME]!!,
        description = record[INGREDIENT.DESCRIPTION]!!,
    )
}

@Repository
open class SqlIngredientRepository(
    dsl: DSLContext,
    instantFactory: ObjectFactory<Instant>,
    log: Logger = LoggerFactory.forClass(),
) :
    SqlCrudRepository<Ingredient, IngredientRecord>(
        INGREDIENT,
        INGREDIENT.ID,
        dsl,
        Ingredient::class,
        instantFactory,
        log,
    ),
    IngredientRepository {

    override fun toModel(record: Record) = toIngredient(record)
}
