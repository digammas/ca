package co.digamma.ca.persistence.sql.food

import co.digamma.ca.persistence.sql.media.PostgreSQLContainerExtension
import co.digamma.ca.suites.food.CourseRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(PostgreSQLContainerExtension::class)
class SqlCourseRepositoryTest(dsl: DSLContext) : CourseRepositoryTestBase() {
    override val sut = SqlCourseRepository(dsl)
}