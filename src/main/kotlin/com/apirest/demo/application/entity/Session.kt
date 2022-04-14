package com.apirest.demo.application.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document
class Session(
    private var idAgenda: String,
    private var nameAgenda: String,
    private var validity: Date
) {
    @Id
    private lateinit var id: String

    fun getId(): String {
        return this.id
    }
}