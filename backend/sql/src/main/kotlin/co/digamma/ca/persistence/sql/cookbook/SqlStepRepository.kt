package co.digamma.ca.persistence.sql.cookbook

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.spi.cookbook.StepRepository
import co.digamma.ca.persistence.jooq.cookbook.tables.records.StepRecord
import co.digamma.ca.persistence.jooq.cookbook.tables.references.RECIPE
import co.digamma.ca.persistence.jooq.cookbook.tables.references.STEP
import co.digamma.ca.persistence.jooq.food.tables.references.DISH
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.util.Locale
import java.util.logging.Logger
import org.jooq.DSLContext
import org.jooq.Path
import org.jooq.Record
import org.springframework.stereotype.Repository

fun toStep(record: Record): Step {
    val recipe = toRecipe(record)
    return Step(
        id = record[STEP.ID]!!,
        locale = Locale.of(record[STEP.LOCALE]),
        description = record[STEP.DESCRIPTION]!!,
        estimatedTime = record[STEP.ESTIMATED_TIME]!!,
        recipe = recipe,
    )
}

@Repository
open class SqlStepRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()
) :
    SqlCrudRepository<Step, StepRecord>(STEP, STEP.ID, dsl, Step::class, log),
    StepRepository {

    override val joinPaths: List<Path<*>> = listOf(
        STEP.recipe,
        STEP.recipe.dish,
        STEP.recipe.dish.course,
        STEP.recipe.dish.cuisine,
        STEP.recipe.dish.serving,
        STEP.recipe.userAccount,
    )

    override fun toRecord(model: Step): StepRecord {
        return super.toRecord(model).also {
            it.recipeId = model.recipe.id
        }
    }

    override fun toModel(record: Record) = toStep(record)
}

