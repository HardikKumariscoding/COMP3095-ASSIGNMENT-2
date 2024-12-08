package ca.gbc.gatewayapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class SwaggerAggregatorController {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${microservices.swagger-urls}")
    private List<String> swaggerUrls; // Configure these in application.properties

    public SwaggerAggregatorController(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Operation()
    @GetMapping("/swagger-aggregator")
    public Mono<ResponseEntity<String>> aggregateSwaggerDocs() {
        // Fetch Swagger JSON from each microservice and collect the responses
        List<Mono<String>> swaggerDocs = swaggerUrls.stream()
                .map(url -> webClient.get().uri(url).retrieve().bodyToMono(String.class))
                .toList();

        // Aggregate the Swagger documentation
        return Mono.zip(swaggerDocs, responses -> {
            // Initialize the aggregated Swagger JSON
            StringBuilder aggregatedJson = new StringBuilder();
            aggregatedJson.append("{");
            aggregatedJson.append("\"swagger\": \"2.0\",");
            aggregatedJson.append("\"paths\": {");

            // Process each microservice's Swagger JSON and extract only the "paths"
            for (Object response : responses) {
                String swaggerJson = (String) response;

                // Extract the "paths" section from the Swagger JSON
                String pathsSection = swaggerJson
                        .replaceAll("\\{.*\"paths\": \\{", "") // Remove everything before "paths"
                        .replaceAll("\\}\\}", "");            // Remove closing braces

                // Append the extracted paths to the aggregated JSON
                aggregatedJson.append(pathsSection).append(",");
            }

            // Remove the trailing comma from the last path entry
            if (aggregatedJson.charAt(aggregatedJson.length() - 1) == ',') {
                aggregatedJson.deleteCharAt(aggregatedJson.length() - 1);
            }

            // Close the JSON structure
            aggregatedJson.append("}}");

            try {
                // Pretty print the aggregated JSON
                String prettyPrintedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(aggregatedJson.toString());

                // Return the pretty-printed Swagger documentation as the response
                return ResponseEntity.ok(prettyPrintedJson);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error generating Swagger documentation");
            }
        });
    }
}
