package com.apirest.demo.application.builders

import com.apirest.demo.application.entity.Session
import java.util.*

class SessionBuilder {
    private lateinit var idAgenda: String
    private lateinit var nameAgenda: String
    private var validity: Date? = null

    companion object {
        fun builder(): SessionBuilder {
            return SessionBuilder()
        }
    }

    fun idAgenda(idAgenda: String): SessionBuilder {
        this.idAgenda = idAgenda
        return this
    }

    fun nameAgenda(nameAgenda: String): SessionBuilder {
        this.nameAgenda = nameAgenda
        return this
    }

    fun validity(validity: Date?): SessionBuilder {
        this.validity = validity
        return this
    }

    fun build(): Session {
        return Session(
            idAgenda = this.idAgenda,
            nameAgenda = this.nameAgenda,
            validity = this.validity ?: getDefaultValidity()
        )
    }

    private fun getDefaultValidity(): Date {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, 1)
        return calendar.time
    }
}