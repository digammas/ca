package co.digamma.ca.persistence.sql.media

import co.digamma.ca.media.ImageRepositoryTest
import org.jooq.DSLContext
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(PostgreSQLContainerExtension::class)
class SqlImageRepositoryTest(dsl: DSLContext): ImageRepositoryTest() {

    override val sut = SqlImageRepository(dsl)
}