package co.digamma.ca.transactions

import co.digamma.ca.persistence.sql.SqlConfiguration
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import java.sql.Connection
import java.sql.DriverManager
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.PostgreSQLContainer


val postgres = PostgreSQLContainer("postgres:16-alpine")

@Configuration
open class TestDSLContextConfiguration {

    @PostConstruct
    fun setup() {
        postgres.start()

    }

    @Bean
    open fun dsl(): DSLContext {
        val connection: Connection = DriverManager.getConnection(postgres.jdbcUrl, postgres.username, postgres.password)
        val configuration = DefaultConfiguration()
        configuration.setSQLDialect(SQLDialect.POSTGRES)
        configuration.setConnection(connection)
        SqlConfiguration().jooqConfigurationCustomizer().customize(configuration)
        return DSL.using(configuration)
    }

    @PreDestroy
    fun tearDown() {
        postgres.close()
    }

}
