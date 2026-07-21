package co.digamma.ca.transactions

import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.support.SimpleTransactionScope

@Component
class SimpleTransactionFactoryPostProcessor : BeanFactoryPostProcessor {

    override fun postProcessBeanFactory(factory: ConfigurableListableBeanFactory) {
        factory.registerScope(TRANSACTION_SCOPE_NAME, SimpleTransactionScope())
    }
}