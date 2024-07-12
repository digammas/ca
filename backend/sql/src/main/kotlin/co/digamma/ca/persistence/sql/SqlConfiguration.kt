package co.digamma.ca.persistence.sql

import org.jooq.conf.RenderQuotedNames
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SqlConfiguration {

    @Bean
    open fun jooqConfigurationCustomizer() = DefaultConfigurationCustomizer {
        with(it.settings()) {
            // Required to avoid case-sensitivity issues due to H2 being used for code generation from chang logs
            renderQuotedNames = RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED
            // Required to allow mapping into Kotlin classes
            isMapConstructorParameterNamesInKotlin = true
        }

    }
}
