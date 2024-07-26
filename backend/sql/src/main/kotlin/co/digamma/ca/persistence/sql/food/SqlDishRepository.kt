package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.persistence.jooq.food.tables.records.DishRecord
import co.digamma.ca.persistence.jooq.food.tables.references.DISH
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.util.Locale
import java.util.logging.Logger
import org.jooq.DSLContext
import org.jooq.Path
import org.jooq.Record
import org.springframework.stereotype.Repository

fun toDish(record: Record): Dish {
    val course = toCourse(record)
    val cuisine = toCuisine(record)
    val serving = toServing(record)
    return Dish(
        id = record[DISH.ID]!!,
        locale = Locale.of(record[DISH.LOCALE]),
        name = record[DISH.NAME]!!,
        course = course,
        cuisine = cuisine,
        serving = serving,
    )
}

@Repository
class SqlDishRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()
) :
    SqlCrudRepository<Dish, DishRecord>(DISH, DISH.ID, dsl, Dish::class, log),
    DishRepository {

    override val joinPaths: List<Path<*>> = listOf(
        DISH.course,
        DISH.cuisine,
        DISH.serving,
    )

    override fun toRecord(model: Dish): DishRecord {
        return super.toRecord(model).also {
            it.courseId = model.course.id
            it.cuisineId = model.cuisine.id
            it.servingId = model.serving.id
        }
    }

    override fun toModel(record: Record) = toDish(record)
}