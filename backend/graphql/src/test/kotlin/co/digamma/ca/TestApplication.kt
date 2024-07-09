package co.digamma.ca

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(includeFilters = [ComponentScan.Filter(Singleton::class)])
open class TestApplication