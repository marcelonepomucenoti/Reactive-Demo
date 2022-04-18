package com.apirest.demo.application.router

import com.apirest.demo.application.dto.SessionRequestDTO
import com.apirest.demo.application.dto.SessionResponseDTO
import com.apirest.demo.application.handler.SessionHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration

class SessionRoutes {

    private val baseUri: String = "/session"

    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/session",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.GET),
            beanClass = SessionHandler::class,
            beanMethod = "findAll",
            operation = Operation(
                operationId = "findAll",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "200",
                        description = "Successful - returns an array of type SessionResponseDTO, which can be an empty array",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = SessionResponseDTO::class
                                )
                            )
                        )
                    ),
                    ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error - A generic error message, given when an unexpected condition was encountered and no more specific message is suitable",
                    )
                )
            )
        ),
        RouterOperation(
            path = "/session/{id}",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.GET),
            beanClass = SessionHandler::class,
            beanMethod = "findById",
            operation = Operation(
                operationId = "findById",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "200",
                        description = "Successful - returns an object of type SessionResponseDTO, which can be an empty object",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = SessionResponseDTO::class
                                )
                            )
                        )
                    ),
                    ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error - A generic error message, given when an unexpected condition was encountered and no more specific message is suitable",
                    )
                ),
                parameters = arrayOf(
                    Parameter(
                        name = "id",
                        `in` = ParameterIn.PATH
                    )
                )
            )
        ),
        RouterOperation(
            path = "/session",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.POST),
            beanClass = SessionHandler::class,
            beanMethod = "save",
            operation = Operation(
                operationId = "save",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "201",
                        description = "Successful - Returns an object of type SessionResponseDTO",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = SessionResponseDTO::class
                                )
                            )
                        )
                    ),
                    ApiResponse(
                        responseCode = "422",
                        description = "Agenda already has session!",
                    ),
                    ApiResponse(
                        responseCode = "422",
                        description = "Unidentified agenda!",
                    ),
                    ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error - A generic error message, given when an unexpected condition was encountered and no more specific message is suitable",
                    )
                ),
                requestBody = RequestBody(
                    content = arrayOf(
                        Content(
                            schema = Schema(
                                implementation = SessionRequestDTO::class,
                            )
                        )
                    )
                )
            )
        )
    )
    fun sessionRouter(handler: SessionHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(GET(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
            .andRoute(GET("$baseUri/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
            .andRoute(POST(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::save)
    }
}