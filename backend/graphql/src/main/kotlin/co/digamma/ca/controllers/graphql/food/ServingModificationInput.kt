package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.ServingModification


data class ServingModificationInput(
    private val id: String,
    private val name: String?,
    private val temperatureMin: Int?,
    private val temperatureMax: Int?,
) {

    fun toServingModification() = ServingModification (
        id = this.id,
        name = this.name,
        temperature = if (temperatureMin != null && temperatureMax != null) {
            IntRange(temperatureMin, temperatureMax)
        } else null,
    )
}
