package com.apirest.demo.application.handler

import com.apirest.demo.application.dto.AssociateRequestDTO
import com.apirest.demo.application.entity.Associate
import com.apirest.demo.application.service.AssociateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import java.net.URI

@Component
class AssociateHandler(@Autowired val associateService: AssociateService) {

    fun save(request: ServerRequest): Mono<ServerResponse> {
        val associateRequestDTO = request.bodyToMono<AssociateRequestDTO>()
        return created(URI.create("teste"))
            .contentType(MediaType.APPLICATION_JSON)
            .body(fromPublisher(associateRequestDTO.flatMap(associateService::save), Associate::class.java))
    }

}