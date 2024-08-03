package co.digamma.ca.transactions

import kotlin.test.assertEquals
import org.jooq.DSLContext
import org.jooq.impl.DSL.table
import org.jooq.impl.SQLDataType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [
    ApplicationTransactionAttributeSource::class,
    TransactionalService::class,
    TestDSLContextConfiguration::class
])
class ApplicationTransactionAttributeSourceTest {

    @Autowired
    private lateinit var transactionalService: TransactionalService

    @Autowired
    private lateinit var dsl: DSLContext

    @BeforeEach
    fun setUp() {
        dsl.createTableIfNotExists(table("test"))
            .column("id", SQLDataType.INTEGER)
            .column("name", SQLDataType.VARCHAR(255))
            .execute()
    }

    @Test
    fun `should rollback transaction`() {
        assertThrows<IllegalStateException> {
            transactionalService.doTransaction()
        }
        val count = dsl.fetchCount(table("test"))
        assertEquals(0, count)
    }
}
