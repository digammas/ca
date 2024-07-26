package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.persistence.jooq.food.tables.records.CourseRecord
import co.digamma.ca.persistence.jooq.food.tables.references.COURSE
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.util.Locale
import java.util.logging.Logger
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

fun toCourse(record: Record): Course {
    return Course(
        id = record[COURSE.ID]!!,
        locale = Locale.of(record[COURSE.LOCALE]),
        name = record[COURSE.NAME]!!,
    )
}

@Repository
class SqlCourseRepository(
    dsl: DSLContext,
    log: Logger = LoggerFactory.forClass()
) :
    SqlCrudRepository<Course, CourseRecord>(COURSE, COURSE.ID, dsl, Course::class, log),
    CourseRepository {

    override fun toModel(record: Record) = toCourse(record)
}