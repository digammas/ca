package co.digamma.ca.transactions

import org.springframework.context.annotation.Scope

const val TRANSACTION_SCOPE_NAME = "transaction"

@Scope(value = TRANSACTION_SCOPE_NAME)
annotation class TransactionScope
