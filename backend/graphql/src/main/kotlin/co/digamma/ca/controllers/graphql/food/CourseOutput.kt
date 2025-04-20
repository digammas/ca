package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.Course

data class CourseOutput(
    val id: String,
    val locale: String,
    val name: String,
)

fun Course.toCourseOutput() = CourseOutput(
    id = this.id,
    locale = this.locale.toString(),
    name = this.name,
)
