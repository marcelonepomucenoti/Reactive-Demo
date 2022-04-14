package com.apirest.demo.application.handler

import com.apirest.demo.application.dto.VotesRequestDTO
import com.apirest.demo.application.entity.Votes
import com.apirest.demo.application.service.VotesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import reactor.core.publisher.Mono
import java.net.URI

@Component
class VotesHandler(@Autowired val votesService: VotesService) {
    fun save(request: ServerRequest): Mono<ServerResponse> {
        val votesRequestDTO = request.bodyToMono<VotesRequestDTO>()
        return ServerResponse.created(URI.create("teste"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(votesRequestDTO.flatMap(votesService::save), Votes::class.java))
    }
}