package co.digamma.ca.persistence.sql.media

import co.digamma.ca.suites.media.ImageRepositoryTestBase
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(PostgreSQLContainerExtension::class)
class SqlImageRepositoryTest(dsl: DSLContext) : ImageRepositoryTestBase() {

    override val sut = SqlImageRepository(dsl)
}