package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.ServingModification


data class ServingModificationInput(
    private val id: String,
    private val name: String,
) {

    fun toServingModification() = ServingModification (
        id = this.id,
        name = this.name,
    )
}
