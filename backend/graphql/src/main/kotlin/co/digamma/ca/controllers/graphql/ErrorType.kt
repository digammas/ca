package co.digamma.ca.controllers.graphql

import graphql.ErrorClassification

enum class ErrorType : ErrorClassification {
    BAD_REQUEST,
    CONFLICT,
    NOT_FOUND,
    INTERNAL_ERROR,
}