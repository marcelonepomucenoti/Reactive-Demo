package com.apirest.demo.application.router

import com.apirest.demo.application.dto.AgendaRequestDTO
import com.apirest.demo.application.dto.AgendaResponseDTO
import com.apirest.demo.application.handler.AgendaHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class AgendaRoutes {

    private val baseUri: String = "/agenda"

    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/agenda",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.GET),
            beanClass = AgendaHandler::class,
            beanMethod = "findAll",
            operation = Operation(
                operationId = "findAll",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "200",
                        description = "Successful - returns an array of type AgendaResponseDTO, which can be an empty array",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = AgendaResponseDTO::class
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
            path = "/agenda/{id}",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.GET),
            beanClass = AgendaHandler::class,
            beanMethod = "findById",
            operation = Operation(
                operationId = "findById",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "200",
                        description = "Successful - returns an object of type AgendaResponseDTO, which can be an empty object",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = AgendaResponseDTO::class
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
            path = "/agenda",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.POST),
            beanClass = AgendaHandler::class,
            beanMethod = "save",
            operation = Operation(
                operationId = "save",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "201",
                        description = "Successful - Returns an object of type AgendaResponseDTO",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = AgendaResponseDTO::class
                                )
                            )
                        )
                    ),
                    ApiResponse(
                        responseCode = "422",
                        description = "Unprocessable Entity - Agenda already registered!",
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
                                implementation = AgendaRequestDTO::class
                            )
                        )
                    )
                )
            )
        ),
        RouterOperation(
            path = "/agenda/calculateResult/{id}",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.PUT),
            beanClass = AgendaHandler::class,
            beanMethod = "calculateResult",
            operation = Operation(
                operationId = "calculateResult",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "200",
                        description = "Successful - returns an object of type AgendaResponseDTO",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = AgendaResponseDTO::class
                                )
                            )
                        )
                    ),
                    ApiResponse(
                        responseCode = "422",
                        description = "Agenda has already been accounted for!",
                    ),
                    ApiResponse(
                        responseCode = "422",
                        description = "Session not opened for this agenda!",
                    ),
                    ApiResponse(
                        responseCode = "422",
                        description = "Agenda can still receive votes!",
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
        )
    )
    fun agendaRouter(handler: AgendaHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(GET(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
            .andRoute(GET("$baseUri/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
            .andRoute(POST(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::save)
            .andRoute(
                PUT("$baseUri/calculateResult/{id}").and(accept(MediaType.APPLICATION_JSON)),
                handler::calculateResult
            )
    }
}