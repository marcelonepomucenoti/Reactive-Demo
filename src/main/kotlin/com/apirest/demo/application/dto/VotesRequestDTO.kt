package com.apirest.demo.application.dto

class VotesRequestDTO(
    val idAssociate: String,
    val idAgenda: String,
    val idSession: String,
    val vote: Boolean
)