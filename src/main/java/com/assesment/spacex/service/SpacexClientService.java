package com.assesment.spacex.service;

import com.assesment.spacex.domain.Launch;
import com.assesment.spacex.domain.LaunchPad;
import com.assesment.spacex.domain.Rocket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SpacexClientService {
    private final WebClient spacexWebClient;

    /**
     * Fetches data from the SpaceX API's /launches endpoint.
     * <p>
     * This method retrieves all launch data from the SpaceX API and maps the response to a {@link Flux} of {@link Launch} POJOs.
     * Only the necessary properties for launches are extracted based on the {@link Launch} class.
     * </p>
     *
     * @return a {@link Flux} containing a stream of {@link Launch} objects representing the launches retrieved from the API
     */
    public Flux<Launch> getAllLaunches() {
        return spacexWebClient.get().uri("/launches")
                .retrieve()
                .bodyToFlux(Launch.class);
    }

    /**
     * Fetches Rocket information by rocketId from the SpaceX API's /rockets/{rocketId} endpoint.
     * <p>
     * This method retrieves a Rocket data from the SpaceX API and maps the response to a {@link Mono} of {@link Rocket} POJOs.
     * Only the necessary properties for Rockets are extracted based on the {@link Rocket} class.
     * This data is used to get rocket name/ type as per task
     * </p>
     * @param rocketId rocket id for which data to be retrieved
     * @return a {@link Mono} containing a {@link Rocket} object representing the Rocket data retrieved from the API
     */
    public Mono<Rocket> getRocketById(String rocketId) {
        return spacexWebClient.get()
                .uri("/rockets/{rocketId}", rocketId)
                .retrieve()
                .bodyToMono(Rocket.class);
    }

    /**
     * Fetches launchpad information by launchpadId from the SpaceX API's /launchpads/{launchPadId}
     * <p>
     * This method retrieves a LaunchPad data from the SpaceX API and maps the response to a {@link Mono} of {@link LaunchPad} POJOs.
     * Only the necessary properties for LaunchPad are extracted based on the {@link LaunchPad} class.
     * Data is used to get names of launch sites
     * </p>
     * @param launchPadId launch pad id for which data to be retrieved
     * @return mono of {@link LaunchPad}
     */
    public Mono<LaunchPad> getLaunchPadById(String launchPadId) {
        return spacexWebClient.get()
                .uri("/launchpads/{launchPadId}", launchPadId)
                .retrieve()
                .bodyToMono(LaunchPad.class);
    }

}
