package com.apirest.demo.application.repository

import com.apirest.demo.application.entity.Session
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface SessionRepository : ReactiveMongoRepository<Session, String>{
    fun findByIdAgenda(idAgenda: String): Mono<Session>
}