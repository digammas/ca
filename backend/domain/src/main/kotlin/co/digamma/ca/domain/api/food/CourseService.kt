package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.CrudService
import co.digamma.ca.domain.api.model.ModelReference
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
    override val id: String,
    override val name: String?,
) : CourseMutation, ModelReference<Course>
