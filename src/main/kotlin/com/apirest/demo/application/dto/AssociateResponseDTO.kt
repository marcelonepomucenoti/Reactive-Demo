package com.apirest.demo.application.dto

import com.apirest.demo.application.entity.Associate

class AssociateResponseDTO {
    private var id: String? = null
    private var name: String? = null
    private var cpf: String? = null

    fun getId(): String? {
        return this.id
    }

    fun getName(): String? {
        return this.name
    }

    fun getCpf(): String? {
        return this.cpf
    }

    fun associateToAssociateResponseDTO(associate: Associate) : AssociateResponseDTO{
        this.id = associate.getId()
        this.name = associate.getName()
        this.cpf = associate.getCpf()
        return this
    }
}