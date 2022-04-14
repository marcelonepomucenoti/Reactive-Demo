package com.apirest.demo.application.router

import com.apirest.demo.application.handler.AssociateHandler
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class AssociateRoutes {
    private val baseUri: String = "/associate"

    @Bean
    fun associateRouter(handler: AssociateHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(POST(baseUri).and(accept(MediaType.APPLICATION_JSON)), handler::save)
            .andRoute(GET("$baseUri/findById/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::findById)
            .andRoute(GET("$baseUri/findByCpf/{cpf}").and(accept(MediaType.APPLICATION_JSON)), handler::findByCpf)
    }
}