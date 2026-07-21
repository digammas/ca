package co.digamma.ca.transactions

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser
import org.springframework.transaction.interceptor.TransactionAttributeSource

@Configuration
open class ApplicationTransactionManagementConfiguration : ProxyTransactionManagementConfiguration() {

    @Bean
    override fun transactionAttributeSource(): TransactionAttributeSource = AnnotationTransactionAttributeSource(
        ApplicationTransactionAnnotationParser(),
        SpringTransactionAnnotationParser(),
    )
}
