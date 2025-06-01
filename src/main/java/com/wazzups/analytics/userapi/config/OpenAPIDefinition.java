package com.wazzups.analytics.userapi.config;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@io.swagger.v3.oas.annotations.OpenAPIDefinition(
    info = @Info(
        title = "User Management API",
        version = "1.0",
        description = "Endpoints to import users, statistics and evaluations"
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local Dev server")
    })
public class OpenAPIDefinition {}
