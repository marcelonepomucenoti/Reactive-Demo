package com.apirest.demo.application.service

import com.apirest.demo.application.dto.SessionRequestDTO
import com.apirest.demo.application.entity.Session
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface SessionService {
    fun findAll(): Flux<Session>
    fun findById(id: String): Mono<Session>
    fun findByIdAgenda(idAgenda: String): Mono<Session>
    fun save(sessionRequestDTO: SessionRequestDTO): Mono<Session>
}