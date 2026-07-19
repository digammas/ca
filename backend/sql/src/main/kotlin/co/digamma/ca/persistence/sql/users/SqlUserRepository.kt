package co.digamma.ca.persistence.sql.users

import co.digamma.ca.domain.api.common.utils.LoggerFactory
import co.digamma.ca.domain.api.users.User
import co.digamma.ca.domain.spi.users.UserRepository
import co.digamma.ca.persistence.jooq.users.tables.records.UserAccountRecord
import co.digamma.ca.persistence.jooq.users.tables.references.USER_ACCOUNT
import co.digamma.ca.persistence.sql.SqlCrudRepository
import java.time.Instant
import java.util.logging.Logger
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Repository

fun toUser(record: Record): User {
    return User(
        username = record[USER_ACCOUNT.USERNAME]!!,
        password = record[USER_ACCOUNT.PASSWORD]!!,
        email = record[USER_ACCOUNT.EMAIL]!!,
    )
}

@Repository
open class SqlUserRepository(
    dsl: DSLContext,
    instantFactory: ObjectFactory<Instant>,
    log: Logger = LoggerFactory.forClass(),
) :
    SqlCrudRepository<User, UserAccountRecord>(
        USER_ACCOUNT,
        USER_ACCOUNT.USERNAME,
        dsl,
        User::class,
        instantFactory,
        log,
    ),
    UserRepository {

    override fun toModel(record: Record) = toUser(record)
}

