package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.DishCreation
import java.util.Locale


data class DishCreationInput(
    private val locale: String,
    private val name: String,
    private val courseId: String,
    private val cuisineId: String,
    private val servingId: String,
) {

    fun toDishCreation() = DishCreation(
        locale = Locale.forLanguageTag(this.locale),
        name = this.name,
        courseId = this.courseId,
        cuisineId = this.cuisineId,
        servingId = this.servingId,
    )
}
