package co.digamma.ca.persistence.sql.food

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.persistence.jooq.food.tables.records.CourseRecord
import co.digamma.ca.persistence.jooq.food.tables.references.COURSE
import co.digamma.ca.persistence.sql.SqlCrudRepository
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.logging.Logger

fun toCourse(record: Record): Course {
    return Course(
        id = record[COURSE.ID]!!,
        locale = record[COURSE.LOCALE]!!,
        name = record[COURSE.NAME]!!,
    )
}

@Repository
open class SqlCourseRepository(
    dsl: DSLContext,
    instantFactory: ObjectFactory<Instant>,
    log: Logger = LoggerFactory.forClass(),
) :
    SqlCrudRepository<Course, CourseRecord>(
        COURSE,
        COURSE.ID,
        dsl,
        Course::class,
        instantFactory,
        log,
    ),
    CourseRepository {

    override fun toModel(record: Record) = toCourse(record)
}
