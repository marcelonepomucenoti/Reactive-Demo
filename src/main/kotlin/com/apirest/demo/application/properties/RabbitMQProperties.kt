package com.apirest.demo.application.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "rabbitmq.properties")
class RabbitMQProperties(
    var postVoteQueue: String = "",
    var resultVotesQueue: String = "",
    var directExchange: String = ""
)