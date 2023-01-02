package io.iamofoe.dronedispatchservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {
    @Value("${api.common.version}")
    private String apiVersion;
    @Value("${api.common.title}")
    private String apiTitle;
    @Value("${api.common.description}")
    private String apiDescription;
    @Value("${api.common.contact.name}")
    private String apiContactName;
    @Value("${api.common.contact.email}")
    private String apiContactEmail;

    @Bean
    public OpenAPI getOpenAPIDocumentation() {
        return new OpenAPI()
                .info(new Info().title(apiTitle)
                        .description(apiDescription)
                        .version(apiVersion)
                        .contact(new Contact()
                                .name(apiContactName)
                                .email(apiContactEmail)));
    }
}
