package co.digamma.ca.transactions

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser

private val annotationParsers = setOf(
    ApplicationTransactionAnnotationParser(),
    SpringTransactionAnnotationParser(),
)

@Component
class ApplicationTransactionAttributeSource : AnnotationTransactionAttributeSource(annotationParsers)
