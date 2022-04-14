package com.apirest.demo.application.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Agenda(
    private var name: String,
) {
    @Id
    private lateinit var id: String

    fun getId(): String {
        return this.id
    }

    fun getName(): String {
        return this.name
    }
}