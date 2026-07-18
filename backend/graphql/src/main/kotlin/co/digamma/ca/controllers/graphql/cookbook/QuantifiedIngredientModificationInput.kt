package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.QuantifiedIngredientModification

data class QuantifiedIngredientModificationInput(
    private val id: String,
    private val measurementUnitId: String?,
    private val quantity: Float?,
    private val order: Int?,
) {

    fun toQuantifiedIngredientModification() = QuantifiedIngredientModification(
        id = this.id,
        measurementUnitId = this.measurementUnitId,
        quantity = this.quantity,
        order = this.order,
    )
}
