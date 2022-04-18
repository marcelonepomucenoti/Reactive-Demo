package com.apirest.demo.application.service

import com.apirest.demo.application.dto.AssociateRequestDTO
import com.apirest.demo.application.dto.AssociateResponseDTO
import reactor.core.publisher.Mono

interface AssociateService {
    fun save(associateRequestDTO: AssociateRequestDTO): Mono<AssociateResponseDTO>
    fun findById(id: String): Mono<AssociateResponseDTO>
    fun findByCpf(cpf: String): Mono<AssociateResponseDTO>
}