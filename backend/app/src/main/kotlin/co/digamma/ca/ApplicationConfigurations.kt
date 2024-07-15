package co.digamma.ca

import java.util.logging.Logger
import org.springframework.beans.factory.InjectionPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ApplicationConfigurations {

    /**
     * Log provider.
     *
     * @param[ip] injection point.
     */
    @Bean
    open fun log(ip: InjectionPoint): Logger {
        return Logger.getLogger(ip.member.declaringClass.name)
    }
}