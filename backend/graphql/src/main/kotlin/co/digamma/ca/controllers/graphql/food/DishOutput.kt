package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.controllers.graphql.media.ImageOutput
import co.digamma.ca.controllers.graphql.media.toImageOutput
import co.digamma.ca.domain.api.food.Dish

data class DishOutput(
    val id: String,
    val locale: String,
    val name: String,
    val course: CourseOutput,
    val serving: ServingOutput,
    val cuisine: CuisineOutput,
    val images: List<ImageOutput>,
)

fun Dish.toDishOutput() = DishOutput(
    id = this.id,
    locale = this.locale.toString(),
    name = this.name,
    course = this.course.toCourseOutput(),
    serving = this.serving.toServingOutput(),
    cuisine = this.cuisine.toCuisineOutput(),
    images = this.images.map { it.toImageOutput() },
)
