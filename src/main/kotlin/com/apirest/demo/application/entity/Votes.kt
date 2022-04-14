package com.apirest.demo.application.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Votes(
    private var idAgenda: String,
    private var idSession: String,
    private var idAssociate: String,
    private var vote: Boolean,
) {
    @Id
    private lateinit var id: String

    fun getIdAgenda(): String {
        return this.idAgenda
    }

    fun getIdSession(): String {
        return this.idSession
    }

    fun getIdAssociate(): String {
        return this.idAssociate
    }

    fun getVote(): Boolean {
        return this.vote
    }
}