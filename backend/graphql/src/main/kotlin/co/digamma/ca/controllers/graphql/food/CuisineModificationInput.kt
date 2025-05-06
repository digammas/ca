package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.CuisineModification


data class CuisineModificationInput(
    private val id: String,
    private val name: String
) {

    fun toCuisineModification() = CuisineModification(
        id = this.id,
        name = this.name,
    )
}
