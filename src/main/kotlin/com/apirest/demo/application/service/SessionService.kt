package com.apirest.demo.application.service

import com.apirest.demo.application.dto.SessionRequestDTO
import com.apirest.demo.application.dto.SessionResponseDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface SessionService {
    fun findAll(): Flux<SessionResponseDTO>
    fun findById(id: String): Mono<SessionResponseDTO>
    fun save(sessionRequestDTO: SessionRequestDTO): Mono<SessionResponseDTO>
}