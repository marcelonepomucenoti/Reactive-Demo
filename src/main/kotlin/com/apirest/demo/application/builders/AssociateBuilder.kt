package com.apirest.demo.application.builders

import com.apirest.demo.application.entity.Associate

class AssociateBuilder {
    private lateinit var name: String
    private lateinit var cpf: String

    companion object {
        fun builder(): AssociateBuilder {
            return AssociateBuilder()
        }
    }

    fun name(name: String): AssociateBuilder {
        this.name = name
        return this
    }

    fun cpf(cpf: String): AssociateBuilder {
        this.cpf = cpf
        return this
    }

    fun build(): Associate {
        return Associate(
            name = this.name,
            cpf = this.cpf
        )
    }
}