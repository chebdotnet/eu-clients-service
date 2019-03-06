package com.eu.client.registration;


import com.google.common.base.Predicate;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class ApiDocumentationConfig {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    Docket publicApi(ApiDocumentationProperties properties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(toApiInfo(properties))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(publicApiPaths())
                .build()
                .pathMapping(properties.getPathMapping())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo toApiInfo(ApiDocumentationProperties properties) {
        return new ApiInfo(properties.getTitle(), properties.getDescription(), properties.getVersion(),
                null, toContact(properties.getContact()), null, null);
    }

    private String toContact(ApiContact apiContact) {
        return apiContact.getName() + " (Email: " + apiContact.getEmail() + ")";
    }

    @SuppressWarnings("Guava")
    private Predicate<String> publicApiPaths() {
        return regex("/api.*");
    }

    @Data
    @Component
    @ConfigurationProperties("api.documentation")
    private static class ApiDocumentationProperties {

        private String title;

        private String description;

        private String version;

        private ApiContact contact;

        private String pathMapping;
    }

    @Data
    private static class ApiContact {

        private String name;

        private String email;
    }
}
