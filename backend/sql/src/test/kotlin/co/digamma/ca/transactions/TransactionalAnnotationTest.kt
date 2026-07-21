package co.digamma.ca.transactions

import co.digamma.ca.domain.api.common.stereotypes.Transactional
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.support.ScopeNotActiveException
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.AbstractPlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionStatus
import org.springframework.transaction.support.TransactionSynchronizationManager

@Transactional
open class TransactionalTarget(private val scopedValueFactory: ObjectFactory<Long>) {
    open fun isSynchronizationActive(): Boolean = TransactionSynchronizationManager.isSynchronizationActive()
    open fun resolveScopedValue(): Long = scopedValueFactory.getObject()
}

open class NonTransactionalTarget(private val scopedValueFactory: ObjectFactory<Long>) {
    open fun resolveScopedValue(): Long = scopedValueFactory.getObject()
}

private class NoOpTransactionManager : AbstractPlatformTransactionManager() {
    override fun doGetTransaction(): Any = Any()
    override fun doBegin(transaction: Any, definition: TransactionDefinition) {}
    override fun doCommit(status: DefaultTransactionStatus) {}
    override fun doRollback(status: DefaultTransactionStatus) {}
}

@Configuration
open class TransactionalAnnotationTestConfig {

    @Bean
    open fun transactionManager(): PlatformTransactionManager = NoOpTransactionManager()

    @TransactionScope
    @Bean
    open fun scopedValue(): Long = System.nanoTime()

    @Bean
    open fun transactionalTarget(scopedValueFactory: ObjectFactory<Long>) =
        TransactionalTarget(scopedValueFactory)

    @Bean
    open fun nonTransactionalTarget(scopedValueFactory: ObjectFactory<Long>) =
        NonTransactionalTarget(scopedValueFactory)
}

class TransactionalAnnotationTest {

    private lateinit var context: AnnotationConfigApplicationContext

    @BeforeEach
    fun setUp() {
        context = AnnotationConfigApplicationContext(
            TransactionalAnnotationTestConfig::class.java,
            SimpleTransactionFactoryPostProcessor::class.java,
            ApplicationTransactionManagementConfiguration::class.java,
            TransactionAutoConfiguration::class.java,
            AopAutoConfiguration::class.java,
        )
    }

    @AfterEach
    fun tearDown() {
        context.close()
    }

    @Test
    fun `method annotated with the custom Transactional runs inside an active transaction`() {
        val target = context.getBean(TransactionalTarget::class.java)
        assertTrue(target.isSynchronizationActive())
    }

    @Test
    fun `method annotated with the custom Transactional can resolve transaction-scoped beans`() {
        val target = context.getBean(TransactionalTarget::class.java)
        target.resolveScopedValue()
    }

    @Test
    fun `method without the custom Transactional cannot resolve transaction-scoped beans`() {
        val target = context.getBean(NonTransactionalTarget::class.java)
        assertThrows(ScopeNotActiveException::class.java) {
            target.resolveScopedValue()
        }
    }
}
