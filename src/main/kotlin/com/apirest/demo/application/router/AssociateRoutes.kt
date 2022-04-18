package com.apirest.demo.application.router

import com.apirest.demo.application.dto.AssociateRequestDTO
import com.apirest.demo.application.dto.AssociateResponseDTO
import com.apirest.demo.application.handler.AssociateHandler
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
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class AssociateRoutes {
    private val baseUri: String = "/associate"

    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/associate/findById/{id}",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.GET),
            beanClass = AssociateHandler::class,
            beanMethod = "findById",
            operation = Operation(
                operationId = "findById",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "200",
                        description = "Successful - returns an object of type AssociateResponseDTO, which can be an empty object",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = AssociateResponseDTO::class
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
            path = "/associate/findByCpf/{cpf}",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.GET),
            beanClass = AssociateHandler::class,
            beanMethod = "findByCpf",
            operation = Operation(
                operationId = "findByCpf",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "200",
                        description = "Successful - returns an object of type AssociateResponseDTO, which can be an empty object",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = AssociateResponseDTO::class
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
                        name = "cpf",
                        `in` = ParameterIn.PATH
                    )
                )
            )
        ),
        RouterOperation(
            path = "/associate",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.POST),
            beanClass = AssociateHandler::class,
            beanMethod = "save",
            operation = Operation(
                operationId = "save",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "201",
                        description = "Successful - returns an object of type AssociateResponseDTO, which can be an empty object",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = AssociateResponseDTO::class
                                )
                            )
                        )
                    ),
                    ApiResponse(
                        responseCode = "422",
                        description = "member already registered!",
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
                                implementation = AssociateRequestDTO::class
                            )
                        )
                    )
                )
            )
        )
    )
    fun associateRouter(handler: AssociateHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(POST(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::save)
            .andRoute(GET("$baseUri/findById/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
            .andRoute(GET("$baseUri/findByCpf/{cpf}").and(accept(MediaType.APPLICATION_JSON)), handler::findByCpf)
    }
}