package com.apirest.demo.application.vo

class ValidateCpfResponseVo(private val status: String) {
    fun getStatus(): String {
        return this.status
    }

    fun canVote(): Boolean {
        return this.status == "ABLE_TO_VOTE"
    }
}