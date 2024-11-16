package com.assesment.spacex.router;

import com.assesment.spacex.handler.TaskHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TaskRouter {

    /**
     * Configures the routing logic for the application using Spring WebFlux's functional programming model.
     *
     * This bean defines the routes for handling API requests, mapping HTTP endpoints to their corresponding
     * handlers. It ensures that the appropriate solution logic is invoked for each endpoint.
     *
     * <p>Routes defined:</p>
     * <ul>
     *   <li><b>GET /task/rocket/launches-by-year</b>: Retrieves the number of rocket launches grouped by year for each rocket.</li>
     *   <li><b>GET /task/rocket/launches-by-site</b>: Retrieves the number of rocket launches grouped by launch site for each rocket.</li>
     *   <li><b>GET /b>:Just returs a string which welcomes the user to data solution</li>
     * </ul>
     *
     * <p>Example usage:</p>
     * <pre>
     * curl -X GET http://localhost:8080/task/rocket/launches-by-year
     * curl -X GET http://localhost:8080/task/rocket/launches-by-site
     * curl -X GET http://localhost:8080/
     * </pre>
     * @param taskHandler {@link TaskHandler}
     * @return a {@link RouterFunction} mapping the API endpoints to their respective handlers
     */
    @Bean
    public RouterFunction<ServerResponse> taskSolutionRouter(TaskHandler taskHandler){

        return route()
                .path("/task/rocket",
                        builder -> builder
                                .GET("/launches-by-year", taskHandler::getRocketsLaunchedByYear)
                                .GET("/launches-by-site", taskHandler::getLaunchesPerSite)
                )
                .GET("/", taskHandler::welcomeSpacexSolution)
                .after(this::logRequest)
                .build();
    }

    /**
     * Logs the request information and response status after the api is called.
     * @param request a {@link ServerRequest}
     * @param response a {@link ServerResponse}
     * @return a {@link ServerResponse}
     */
    private ServerResponse logRequest(ServerRequest request, ServerResponse response) {
        log.info("Request method %s path %s is called".formatted(request.method(), request.path()));
        log.info("Response is status %s".formatted(response.statusCode()));
        return response;
    }


}
