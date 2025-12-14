package com.kimhok.tickets.config.utils;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ticket Flow API")
                        .description("Build the scalable API with Spring Boot Docker")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Heng Kimhok")
                                .email("hengkimhok07@gmail.com")
                                .url("https://github.com/Hok1707"))
                ).externalDocs(new ExternalDocumentation()
                        .description("GitHub Repository")
                        .url("https://github.com/Hok1707/tickets-api"))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Dev"),
                        new Server().url("http://157.10.73.192:8080").description("Production")));
    }
}
