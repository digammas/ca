package co.digamma.ca.persistence.sql.media

import co.digamma.ca.persistence.jooq.DefaultCatalog
import co.digamma.ca.persistence.sql.SqlConfiguration
import java.sql.Connection
import java.sql.DriverManager
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.testcontainers.containers.PostgreSQLContainer

private val postgres = PostgreSQLContainer("postgres:16-alpine")

class PostgreSQLContainerExtension : BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private lateinit var dsl: DSLContext

    override fun beforeAll(context: ExtensionContext) {
        postgres.start()
        val connection: Connection = DriverManager.getConnection(postgres.jdbcUrl, postgres.username, postgres.password)
        val configuration = DefaultConfiguration()
        configuration.setSQLDialect(SQLDialect.POSTGRES)
        configuration.setConnection(connection)
        SqlConfiguration().jooqConfigurationCustomizer().customize(configuration)
        this.dsl = DSL.using(configuration)
        this.dsl.ddl(DefaultCatalog.DEFAULT_CATALOG).executeBatch()
    }

    override fun afterAll(context: ExtensionContext?) {
        postgres.close()
    }

    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean = parameterContext.parameter.type == DSLContext::class.java

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): DSLContext = this.dsl

}
