package co.digamma.ca.domain.api.food

import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.RetrieveService
import java.util.Locale

interface CourseService: RetrieveService<Course>, DeleteService {
    fun create(creation: CourseCreation): Course
    fun update(modification: CourseModification): Course
}

interface CourseMutation {
    val name: String?
}

data class CourseCreation(
    val locale: Locale,
    override val name: String,
): CourseMutation

data class CourseModification(
    val id: String,
    override val name: String?,
): CourseMutation