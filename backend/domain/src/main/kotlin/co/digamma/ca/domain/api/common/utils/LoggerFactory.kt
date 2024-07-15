package co.digamma.ca.domain.api.common.utils

import java.util.logging.Logger

/**
 * Logger factory object.
 */
object LoggerFactory {

    fun forClass(): Logger {
        return StackWalker.getInstance()
            .walk { it.skip(1).findFirst() }
            .map { it.className }
            .map { Logger.getLogger(it) }
            .orElseGet { Logger.getGlobal() }
    }
}
