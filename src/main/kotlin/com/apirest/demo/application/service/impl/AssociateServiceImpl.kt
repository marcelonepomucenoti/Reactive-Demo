package com.apirest.demo.application.service.impl

import com.apirest.demo.application.builders.AssociateBuilder
import com.apirest.demo.application.dto.AssociateRequestDTO
import com.apirest.demo.application.dto.AssociateResponseDTO
import com.apirest.demo.application.entity.Associate
import com.apirest.demo.application.repository.AssociateRepository
import com.apirest.demo.application.service.AssociateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class AssociateServiceImpl(@Autowired val associateRepository: AssociateRepository) : AssociateService {

    override fun findByCpf(cpf: String): Mono<AssociateResponseDTO> {
        return associateRepository.findByCpf(cpf).map { AssociateResponseDTO().associateToAssociateResponseDTO(it) }
    }

    override fun findById(id: String): Mono<AssociateResponseDTO> {
        return associateRepository.findById(id).map { AssociateResponseDTO().associateToAssociateResponseDTO(it) }
    }

    override fun save(associateRequestDTO: AssociateRequestDTO): Mono<AssociateResponseDTO> {
        return associateRepository.findByCpf(associateRequestDTO.cpf).mapNotNull { p -> Associate(p.getName(), p.getCpf()) }
            .flatMap<Associate> {
                Mono.error(ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "member already registered!"))
            }.switchIfEmpty {
                val associate: Associate =
                    AssociateBuilder.builder()
                        .name(associateRequestDTO.name)
                        .cpf(associateRequestDTO.cpf)
                        .build()
                associateRepository.save(associate)
            }.map { AssociateResponseDTO().associateToAssociateResponseDTO(it) }
    }
}