package com.example.khetihisab.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI khetiHisabOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KhetiHisab API")
                        .description("""
                                REST APIs for managing agricultural services,
                                customers, drivers, cultivation jobs, payments
                                and outstanding balances.
                                """)
                        .version("v1")
                        .license(new License()
                                .name("Internal Use")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ));
    }
}
