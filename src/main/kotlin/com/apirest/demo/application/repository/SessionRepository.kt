package com.apirest.demo.application.repository

import com.apirest.demo.application.entity.Session
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface SessionRepository : ReactiveMongoRepository<Session, String>