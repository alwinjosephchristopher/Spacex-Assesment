package com.assesment.spacex.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@Slf4j
public class SpacexWebClientConfig {

    /**
     * Configures and provides a WebClient bean for making API requests to SpaceX's exposed APIs.
     * <p>
     * The base URI for SpaceX is fetched from the application's configuration (application.yaml)
     * based on the active Spring profile. The WebClient is configured with a default JSON content type
     * for the API requests.
     * </p>
     *
     * <p>logged baser uri for debugging purposes</p>
     *
     * @param baseUri the base URI for SpaceX API, sourced from the application.yaml configuration file
     *                based on the active Spring profile.
     * @return a configured {@link WebClient} instance for interacting with SpaceX APIs
     */
    @Bean
    public WebClient spacexWebClient(@Value("${spacex.baseUri}") String baseUri) {
        log.info("Base uri being used - %s".formatted(baseUri));
        return WebClient.builder()
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .baseUrl(baseUri)
                .build();
    }
}
