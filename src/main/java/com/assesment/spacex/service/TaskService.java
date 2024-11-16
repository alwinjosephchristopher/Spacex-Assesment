package com.assesment.spacex.service;

import com.assesment.spacex.domain.Launch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final SpacexClientService spacexClientService;

    /**
     * Retrieves the count of rockets launched by year, grouped by rocket type/name.
     * <p>
     * This method first fetches all launches using the SpaceX API, then enriches each launch
     * with its corresponding rocket's name. If the rocket name is unavailable, a default value
     * "ROCKET_NAME_NA" is assigned. After enriching the data, it groups the launches by rocket
     * type and year, returning the count of launches for each rocket type by year.
     * </p>
     *
     * @return a {@link Mono} containing a map where the keys are rocket types/names, and the values are
     *         maps of years to launch counts (e.g., Map<rocketType, Map<year, count>>).
     */
    public Mono<Map<String, Map<Integer, Long>>> getRocketsLaunchedByYear() {
         Mono<List<Launch>> launchListMono = spacexClientService.getAllLaunches()
                .flatMap(launch ->
                    spacexClientService.getRocketById(launch.getRocketId())
                            .map(rocket -> {
                                launch.setRocketType(isNull(rocket.getRocketName())
                                        ? "ROCKET_NAME_NA"
                                        : rocket.getRocketName());
                                return launch;
                })).collectList();

         return launchListMono.map(launches -> launches.stream()
               .collect(Collectors.groupingBy(
                       Launch::getRocketType,
                       Collectors.groupingBy(
                               launch -> launch.getDate().atZone(ZoneId.of("UTC")).getYear(),
                                Collectors.counting())
                       )
                    ));

    }

    /**
     * Retrieves the count of launches per launch site, grouped by rocket type.
     * <p>
     * This method first fetches all launches using the SpaceX API, then enriches each launch with
     * its corresponding launch pad and rocket information. If the launch pad name or rocket name
     * is unavailable, default values ("LAUNCHPAD_NAME_NA" and "ROCKET_NAME_NA") are assigned.
     * After enriching the data, the method groups the launches by rocket type and launch pad,
     * returning the count of launches for each combination of rocket type and launch pad.
     * </p>
     *
     * @return a {@link Mono} containing a map where the keys are rocket types, and the values are
     *         maps of launch pad names to launch counts (e.g., Map<rocketType, Map<launchPadName, count>>).
     */
    public Mono<Map<String, Map<String, Long>>> getLaunchesPerSite() {
        Mono<List<Launch>> listMono = spacexClientService.getAllLaunches()
                .flatMap(launch ->
                    spacexClientService.getLaunchPadById(launch.getLaunchPadId())
                    .zipWith(spacexClientService.getRocketById(launch.getRocketId()))
                    .map(tuple -> {
                        launch.setLaunchPadName(isNull(tuple.getT1().getLaunchPadName())
                                ? "LAUNCHPAD_NAME_NA"
                                : tuple.getT1().getLaunchPadName());
                        launch.setRocketType(isNull(tuple.getT2().getRocketName())
                                ? "ROCKET_NAME_NA"
                                : tuple.getT2().getRocketName()
                        );
                        return launch;
                })).collectList();

        return listMono.map(launches ->  launches.stream()
                    .collect(Collectors.groupingBy(
                            Launch::getRocketType,
                            Collectors.groupingBy(
                                    Launch::getLaunchPadName,
                                    Collectors.counting())
            ))
        );
    }
}
