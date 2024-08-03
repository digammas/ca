package co.digamma.ca.transactions

import co.digamma.ca.domain.api.common.stereotypes.Transactional
import org.jooq.DSLContext
import org.jooq.impl.DSL.table
import org.jooq.impl.DSL.field

@Transactional
class TransactionalService(
    private val dsl: DSLContext
) {

    fun doTransaction() {
        dsl.insertInto(table("test"))
            .set(field("id"), 1)
            .set(field("name"), "test")
            .execute()
        throw IllegalStateException()
    }
}
