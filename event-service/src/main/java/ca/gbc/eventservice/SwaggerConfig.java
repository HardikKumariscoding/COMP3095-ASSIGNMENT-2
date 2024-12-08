//package ca.gbc.eventservice;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.License;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Custom API Documentation")
//                        .version("1.0.0")
//                        .description("This is a customized description for the API documentation.")
//                        .termsOfService("http://example.com/terms")
//                        .contact(new Contact()
//                                .name("Your Name")
//                                .email("your-email@example.com")
//                                .url("http://example.com"))
//                        .license(new License()
//                                .name("Apache 2.0")
//                                .url("http://springdoc.org")));
//    }
//}
//
