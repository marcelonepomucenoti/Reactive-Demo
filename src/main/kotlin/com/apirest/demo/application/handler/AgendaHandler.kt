package com.apirest.demo.application.handler

import com.apirest.demo.application.dto.AgendaRequestDTO
import com.apirest.demo.application.entity.Agenda
import com.apirest.demo.application.service.AgendaService
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
class AgendaHandler(@Autowired val agendaService: AgendaService) {

    fun findAll(request: ServerRequest): Mono<ServerResponse> {
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(agendaService.findAll(), Agenda::class.java)
    }

    fun findById(request: ServerRequest): Mono<ServerResponse> {
        val id: String = request.pathVariable("id")
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(agendaService.findById(id), Agenda::class.java)
    }

    fun calculateResult(request: ServerRequest): Mono<ServerResponse> {
        val id: String = request.pathVariable("id")
        return ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(agendaService.calculateResult(id), Agenda::class.java)
    }

    fun save(request: ServerRequest): Mono<ServerResponse> {
        val agendaRequestDTO = request.bodyToMono<AgendaRequestDTO>()
        return created(URI.create("teste"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(agendaRequestDTO.flatMap(agendaService::save), Agenda::class.java))
    }
}