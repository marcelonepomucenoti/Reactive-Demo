package com.apirest.demo.application.service

import com.apirest.demo.application.dto.AssociateRequestDTO
import com.apirest.demo.application.entity.Associate
import reactor.core.publisher.Mono

interface AssociateService {
    fun save(associateRequestDTO: AssociateRequestDTO): Mono<Associate>
    fun findById(id: String): Mono<Associate>
    fun findByCpf(cpf: String): Mono<Associate>
}