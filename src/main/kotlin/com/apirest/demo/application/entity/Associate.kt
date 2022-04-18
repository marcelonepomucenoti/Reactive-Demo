package com.apirest.demo.application.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Associate(
    private var name: String,
    private var cpf: String
) {
    @Id
    private lateinit var id: String

    fun getId(): String {
        return this.id
    }

    fun setId(id: String){
        this.id = id
    }

    fun getName(): String {
        return this.name
    }

    fun getCpf(): String {
        return this.cpf
    }
}