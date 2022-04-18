package com.apirest.demo.application.router

import com.apirest.demo.application.dto.VotesRequestDTO
import com.apirest.demo.application.dto.VotesResponseDTO
import com.apirest.demo.application.handler.VotesHandler
import io.swagger.v3.oas.annotations.Operation
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
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class VotesRoutes {

    private val baseUri: String = "/votes"

    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/votes",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.POST),
            beanClass = VotesHandler::class,
            beanMethod = "save",
            operation = Operation(
                operationId = "save",
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "201",
                        description = "Successful - Returns an object of type VotesResponseDTO",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = VotesResponseDTO::class
                                )
                            )
                        )
                    ),
                    ApiResponse(
                        responseCode = "422",
                        description = "Vote already registered for this member!",
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
                                implementation = VotesRequestDTO::class
                            )
                        )
                    )
                )
            )
        )
    )
    fun votesRouter(handler: VotesHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(POST(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::save)
    }
}