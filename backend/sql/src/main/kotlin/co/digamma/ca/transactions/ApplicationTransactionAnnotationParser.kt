package co.digamma.ca.transactions

import co.digamma.ca.domain.api.common.stereotypes.Transactional
import java.lang.reflect.AnnotatedElement
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.TransactionAnnotationParser
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute
import org.springframework.transaction.interceptor.TransactionAttribute

class ApplicationTransactionAnnotationParser : TransactionAnnotationParser {

    override fun parseTransactionAnnotation(element: AnnotatedElement): TransactionAttribute? {
        val transactional = element.getDeclaredAnnotation(Transactional::class.java) ?: return null
        return RuleBasedTransactionAttribute().also {
            it.isReadOnly = transactional.readOnly
            it.propagationBehavior = if (transactional.nested)
                TransactionDefinition.PROPAGATION_NESTED else TransactionDefinition.PROPAGATION_REQUIRED
            it.isolationLevel = TransactionDefinition.ISOLATION_DEFAULT
        }
    }
}
