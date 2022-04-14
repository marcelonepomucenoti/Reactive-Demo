package com.apirest.demo.application.restClient

import com.apirest.demo.application.vo.ValidateCpfResponseVo
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ValidateCpfWebClient {
    private val webClient: WebClient = WebClient.builder().build()

    fun validateCpf(cpf: String): Mono<ValidateCpfResponseVo> {
        return webClient
            .method(HttpMethod.GET)
            .uri("https://user-info.herokuapp.com/users/$cpf")
            .retrieve()
            .bodyToMono(ValidateCpfResponseVo::class.java)
    }
}