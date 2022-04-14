package com.apirest.demo.application.repository

import com.apirest.demo.application.entity.Associate
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface AssociateRepository : ReactiveMongoRepository<Associate, String>{
    fun findByCpf(cpf: String): Mono<Associate>
}