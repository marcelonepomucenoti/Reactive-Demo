package com.apirest.demo.application.router

import com.apirest.demo.application.handler.AgendaHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class AgendaRoutes {

    private val baseUri: String = "/agenda"

    @Bean
    fun agendaRouter(handler: AgendaHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(GET(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
            .andRoute(GET("$baseUri/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
            .andRoute(POST(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::save)
            .andRoute(PUT("$baseUri/calculateResult/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::calculateResult)
    }
}