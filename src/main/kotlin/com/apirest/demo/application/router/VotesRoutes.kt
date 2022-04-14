package com.apirest.demo.application.router

import com.apirest.demo.application.handler.VotesHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.POST
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class VotesRoutes {

    private val baseUri: String = "/votes"

    @Bean
    fun votesRouter(handler: VotesHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(POST(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::save)
    }
}