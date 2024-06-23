package co.digamma.ca.controllers.graphql

import co.digamma.ca.domain.api.common.DuplicateKeyException
import co.digamma.ca.domain.api.common.NotFoundException
import graphql.ErrorClassification
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class ExceptionAdvice {

    @GraphQlExceptionHandler
    fun handle(ex: NotFoundException, env: DataFetchingEnvironment) = error(ErrorType.NOT_FOUND, ex, env)

    @GraphQlExceptionHandler
    fun handle(ex: DuplicateKeyException, env: DataFetchingEnvironment) = error(ErrorType.CONFLICT, ex, env)

    @GraphQlExceptionHandler
    fun handle(ex: Throwable, env: DataFetchingEnvironment) = error(ErrorType.INTERNAL_ERROR, ex, env)

    private fun error(classification: ErrorClassification, ex: Throwable, env: DataFetchingEnvironment): GraphQLError {
        return GraphqlErrorBuilder.newError()
            .errorType(classification)
            .message(ex.message)
            .path(env.executionStepInfo.path)
            .location(env.field.sourceLocation)
            .build()
    }
}

