package com.apirest.demo.application.router

import com.apirest.demo.application.handler.SessionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
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
    fun sessionRouter(handler: SessionHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(GET(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
            .andRoute(GET("$baseUri/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
            .andRoute(POST(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::save)
    }
}