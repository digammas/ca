package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.controllers.graphql.food.DishOutput
import co.digamma.ca.controllers.graphql.food.toDishOutput
import co.digamma.ca.controllers.graphql.media.ImageOutput
import co.digamma.ca.controllers.graphql.media.toImageOutput
import co.digamma.ca.domain.api.cookbook.Recipe

data class RecipeOutput(
    val id: String,
    val locale: String,
    val dish: DishOutput,
    val yield: Int,
    val createdAt: String,
    val updatedAt: String,
    val author: String,
    val images: List<ImageOutput>,
    val timeToServe: Int,
)

fun Recipe.toRecipeOutput() = RecipeOutput(
    id = this.id,
    locale = this.locale.toString(),
    dish = this.dish.toDishOutput(),
    yield = this.yield,
    createdAt = this.createdAt.value.toString(),
    updatedAt = this.updatedAt.value.toString(),
    author = this.author.username,
    images = this.images.map { it.toImageOutput() },
    timeToServe = this.timeToServe,
)
