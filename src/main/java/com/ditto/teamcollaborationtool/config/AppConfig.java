package com.ditto.teamcollaborationtool.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Team Collaboration Tool")
                        .version("1.0")
                        .description("A REST API to manage teams and projects. Teams can have multiple members, and each team can manage multiple projects with tasks assigned to specific members."));
    }
}
