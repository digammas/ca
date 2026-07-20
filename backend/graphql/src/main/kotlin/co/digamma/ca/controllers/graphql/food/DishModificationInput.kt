package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.DishModification


data class DishModificationInput(
    private val id: String,
    private val name: String?,
    private val courseId: String?,
    private val cuisineId: String?,
    private val servingId: String?,
    private val images: List<String>?,
    private val sideDishes: List<String>?,
) {

    fun toDishModification() = DishModification(
        id = this.id,
        name = this.name,
        courseId = this.courseId,
        cuisineId = this.cuisineId,
        servingId = this.servingId,
        imageIds = this.images,
        sideDishIds = this.sideDishes,
    )
}
