package com.assesment.spacex.handler;

import com.assesment.spacex.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskHandler {

    private final TaskService taskService;

    /**
     * Handles the HTTP GET request to retrieve the number of rocket launches grouped by year.
     * <p></p>This method delegates the computation of launches by year to the {@link TaskService},
     * which processes data from the SpaceX API. The result is then returned as a JSON response
     * with an HTTP 200 OK status.</p>
     *
     * <p>Logs the completion of the operation for monitoring purposes.</p>
     *
     * <p>Example response:</p>
     * <pre>
     * {
     *   "Falcon 9": {
     *     "2020": 12,
     *     "2021": 15
     *   },
     *   "Falcon Heavy": {
     *     "2021": 1
     *   }
     * }
     * </pre>
     *
     * @param serverRequest the incoming request containing any additional parameters (if applicable)
     * @return a {@link Mono} of {@link ServerResponse} containing a map of rocket names to their yearly launch counts
     */
    public Mono<ServerResponse> getRocketsLaunchedByYear(ServerRequest serverRequest) {
        return taskService.getRocketsLaunchedByYear()
                .flatMap(result -> ServerResponse.status(OK).bodyValue(result))
                .doOnNext(response -> log.info("calculated Launches by year"));
    }

    /**
     * Handles the HTTP GET request to retrieve the number of rocket launches grouped by site.
     * <p></p>This method delegates the computation of launches by site to the {@link TaskService},
     * which processes data from the SpaceX API. The result is then returned as a JSON response
     * with an HTTP 200 OK status.</p>
     *
     * <p>Logs the completion of the operation for monitoring purposes.</p>
     *
     * <p>Example response:</p>
     * <pre>
     * {
     *  "Falcon 9": {
     *      "CCAFS SLC 40": 45,
     *      "KSC LC 39A": 52
     *  },
     *  "Falcon Heavy": {
     *      "KSC LC 39A": 3
     *      }
      * }
     * </pre>
     *
     * @param serverRequest the incoming request containing any additional parameters (if applicable)
     * @return a {@link Mono} of {@link ServerResponse} containing a map of rocket names to their site counts
     */
    public Mono<ServerResponse> getLaunchesPerSite(ServerRequest serverRequest) {
        return taskService.getLaunchesPerSite()
                .flatMap(result -> ServerResponse.status(OK).bodyValue(result))
                .doOnNext(response -> log.info("calculated Launches by site"));
    }

    /**
     * Handles the HTTP GET request for the application's base URI.
     * <p>This endpoint serves as a simple welcome message to verify the application's accessibility
     * and ensure that the base URI is functioning as expected.</p>
     *
     * @param request the incoming {@link ServerRequest} containing request details
     * @return a {@link Mono} of {@link ServerResponse} with a welcome message and HTTP 200 OK status
     */

    public Mono<ServerResponse> welcomeSpacexSolution(ServerRequest request) {
        return ServerResponse.status(OK).bodyValue("This is Home of Space X data solution");
    }
}
