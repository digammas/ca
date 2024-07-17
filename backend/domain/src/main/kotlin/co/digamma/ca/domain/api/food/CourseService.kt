package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.CrudService
import java.util.Locale

interface CourseService : CrudService<Course, CourseCreation, CourseModification>

interface CourseMutation {
    val name: String?
}

data class CourseCreation(
    val locale: Locale,
    override val name: String,
) : CourseMutation

data class CourseModification(
    val id: String,
    override val name: String?,
) : CourseMutation