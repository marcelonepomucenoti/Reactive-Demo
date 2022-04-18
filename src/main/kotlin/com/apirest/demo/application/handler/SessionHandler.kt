package com.apirest.demo.application.handler

import com.apirest.demo.application.dto.SessionRequestDTO
import com.apirest.demo.application.dto.SessionResponseDTO
import com.apirest.demo.application.service.SessionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import java.net.URI

@Component
class SessionHandler(@Autowired val sessionService: SessionService) {

    fun findAll(request: ServerRequest): Mono<ServerResponse> {
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(sessionService.findAll(), SessionResponseDTO::class.java))
    }

    fun findById(request: ServerRequest): Mono<ServerResponse> {
        val id: String = request.pathVariable("id")
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(sessionService.findById(id), SessionResponseDTO::class.java))
    }

    fun save(request: ServerRequest): Mono<ServerResponse> {
        val sessionRequestDTO = request.bodyToMono<SessionRequestDTO>()
        return created(URI.create("teste"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(sessionRequestDTO.flatMap(sessionService::save), SessionResponseDTO::class.java))
    }
}