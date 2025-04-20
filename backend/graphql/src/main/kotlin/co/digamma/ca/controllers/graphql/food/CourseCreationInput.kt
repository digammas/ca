package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.CourseCreation
import java.util.Locale

data class CourseCreationInput(
    private val locale: String,
    private val name: String
) {

    fun toCourseCreation() = CourseCreation(
        locale = Locale.forLanguageTag(this.locale),
        name = this.name,
    )
}
