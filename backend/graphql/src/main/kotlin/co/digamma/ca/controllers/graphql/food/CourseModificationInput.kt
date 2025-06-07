package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.CourseModification


data class CourseModificationInput(
    private val id: String,
    private val name: String
) {

    fun toCourseModification() = CourseModification(
        id = this.id,
        name = this.name,
    )
}
