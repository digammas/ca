package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.common.NotFoundException
import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.persistence.jooq.food.tables.records.DishRecord
import co.digamma.ca.persistence.jooq.food.tables.references.DISH
import co.digamma.ca.persistence.jooq.food.tables.references.SIDE_DISH
import co.digamma.ca.persistence.sql.SqlCrudRepository
import org.jooq.DSLContext
import org.jooq.Path
import org.jooq.Record
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.logging.Logger

fun toDish(record: Record): Dish {
    val course = toCourse(record)
    val cuisine = toCuisine(record)
    val serving = toServing(record)
    return Dish(
        id = record[DISH.ID]!!,
        locale = record[DISH.LOCALE]!!,
        name = record[DISH.NAME]!!,
        course = course,
        cuisine = cuisine,
        serving = serving,
    )
}

@Repository
open class SqlDishRepository(
    dsl: DSLContext,
    instantFactory: ObjectFactory<Instant>,
    log: Logger = LoggerFactory.forClass(),
) :
    SqlCrudRepository<Dish, DishRecord>(
        DISH,
        DISH.ID,
        dsl,
        Dish::class,
        instantFactory,
        log,
    ),
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

    override fun create(model: Dish): Dish {
        return super.create(model).also { record ->
            model.sideDishes.forEach {
                this.addSideDish(record, it.id)
            }
        }
    }

    override fun update(model: Dish): Dish {
        return super.update(model).also { updated ->
            updateSideDishes(updated)
        }
    }

    override fun retrieveSideDishes(dishId: String, pageSpecs: PageSpecs): Page<Dish> {
        if (!this.exists(dishId)) {
            throw NotFoundException("No dish with ID $dishId found.")
        }
        val total = this.dsl.selectCount().from(SIDE_DISH)
            .where(SIDE_DISH.MAIN_DISH_ID.eq(dishId))
            .fetchOne(0, Int::class.java)!!
        if (total == 0) {
            return Page(emptyList(), pageSpecs.index, pageSpecs.size)
        }
        val list = this.dsl.select().from(SIDE_DISH)
            .join(DISH)
            .on(SIDE_DISH.SIDE_DISH_ID.eq(DISH.ID))
            .where(SIDE_DISH.MAIN_DISH_ID.eq(dishId))
            .offset(pageSpecs.index * pageSpecs.size)
            .limit(pageSpecs.size)
            .fetch(::toModel)
        return Page(list, pageSpecs.index, pageSpecs.size, total)
    }

    private fun addSideDish(dish: Dish, sideDishId: String) {
        val record = this.dsl.newRecord(SIDE_DISH)
        record.mainDishId = dish.id
        record.sideDishId = sideDishId
        record.timestamp = now()
        this.dsl.executeInsert(record)
    }

    private fun removeSideDish(dish: Dish, sideDishId: String) {
        this.dsl.deleteFrom(SIDE_DISH)
            .where(SIDE_DISH.MAIN_DISH_ID.eq(dish.id))
            .and(SIDE_DISH.SIDE_DISH_ID.eq(sideDishId))
            .limit(1)
            .execute()
    }

    private fun updateSideDishes(model: Dish) {
        val existingSideDishIds = this.dsl.selectFrom(SIDE_DISH)
            .where(SIDE_DISH.MAIN_DISH_ID.eq(model.id))
            .fetch()
            .map { it.sideDishId }
        val sideDishIds = model.sideDishes.map { it.id }
        existingSideDishIds
            .filter { !sideDishIds.contains(it) }
            .forEach { this.removeSideDish(model, it) }
        sideDishIds
            .filter { !existingSideDishIds.contains(it) }
            .forEach { this.addSideDish(model, it) }
    }
}
