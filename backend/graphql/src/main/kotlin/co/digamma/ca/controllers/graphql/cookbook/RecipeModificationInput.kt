package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.RecipeModification


data class RecipeModificationInput(
    private val id: String,
    private val dishId: String?,
    private val yield: Int?,
    private val images: List<String>?,
    private val timeToServe: Int?,
) {

    fun toRecipeModification() = RecipeModification(
        id = this.id,
        dishId = this.dishId,
        yield = this.yield,
        imageIds = this.images ?: emptyList(),
        timeToServe = this.timeToServe,
    )
}
