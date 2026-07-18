package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.IngredientModification


data class IngredientModificationInput(
    private val id: String,
    private val name: String?,
    private val description: String?,
    private val images: List<String>?,
) {

    fun toIngredientModification() = IngredientModification(
        id = this.id,
        name = this.name,
        description = this.description,
        imageIds = this.images,
    )
}
