package com.apirest.demo.application.repository

import com.apirest.demo.application.entity.Agenda
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface AgendaRepository : ReactiveMongoRepository<Agenda, String>{
    fun findByName(name: String): Mono<Agenda>
}