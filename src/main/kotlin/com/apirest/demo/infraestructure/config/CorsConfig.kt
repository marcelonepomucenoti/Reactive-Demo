package com.apirest.demo.infraestructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class CorsConfig(val env: Environment): WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        if (env.acceptsProfiles(Profiles.of("staging")))
            registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")

        if (env.acceptsProfiles(Profiles.of("production")))
            registry.addMapping("/**")
                .allowedOrigins("https://demo.com.br")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
    }
}