package com.assesment.spacex.service;

import com.assesment.spacex.domain.Launch;
import com.assesment.spacex.domain.LaunchPad;
import com.assesment.spacex.domain.Rocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private SpacexClientService spacexClientService;
    @InjectMocks
    private TaskService taskService;


    @Test
    void testRocketLaunchedByYear() {
        Launch launch = getLaunch(Instant.now(), "pad1", "rocket1");
        Rocket rocket = getRocket("rocket1", "Falcon");

        given(spacexClientService.getAllLaunches()).willReturn(Flux.just(launch));
        given(spacexClientService.getRocketById(launch.getRocketId())).willReturn(Mono.just(rocket));

        Map<Integer, Long> yearCount = new HashMap<>();
        yearCount.put(2024, 1L);
        Map<String, Map<Integer, Long>> expected = new HashMap<>();
        expected.put("Falcon", yearCount);

        StepVerifier.create(taskService.getRocketsLaunchedByYear())
                .assertNext(output -> {
                    assertEquals(expected, output);
                })
                .verifyComplete();

        verify(spacexClientService, times(1)).getAllLaunches();
        verify(spacexClientService, times(1)).getRocketById(launch.getRocketId());
    }

    @Test
    void testRocketLaunchedByYearNullRocketName() {
        Launch launch = getLaunch(Instant.now(), "pad1", "rocket1");
        Rocket rocket = getRocket("rocket1", null);

        given(spacexClientService.getAllLaunches()).willReturn(Flux.just(launch));
        given(spacexClientService.getRocketById(launch.getRocketId())).willReturn(Mono.just(rocket));

        Map<Integer, Long> yearCount = new HashMap<>();
        yearCount.put(2024, 1L);
        Map<String, Map<Integer, Long>> expected = new HashMap<>();
        expected.put("ROCKET_NAME_NA", yearCount);

        StepVerifier.create(taskService.getRocketsLaunchedByYear())
                .assertNext(output -> {
                    assertEquals(expected, output);
                })
                .verifyComplete();

        verify(spacexClientService, times(1)).getAllLaunches();
        verify(spacexClientService, times(1)).getRocketById(launch.getRocketId());
    }


    @Test
    void testRocketLaunchedByYearMultipleResults() {
        Launch launch1 = getLaunch(Instant.now(), "pad1", "rocket1");
        Launch launch2 = getLaunch(Instant.parse("2023-01-21T05:47:26.853Z"), "pad2", "rocket2");
        Launch launch3 = getLaunch(Instant.parse("2022-01-21T05:47:26.853Z"), "pad2", "rocket2");

        Rocket rocket1 = getRocket("rocket1", "Falcon");
        Rocket rocket2 = getRocket("rocket2", "Apollo");

        given(spacexClientService.getAllLaunches()).willReturn(Flux.just(launch1,launch2,launch3));
        given(spacexClientService.getRocketById("rocket1")).willReturn(Mono.just(rocket1));
        given(spacexClientService.getRocketById("rocket2")).willReturn(Mono.just(rocket2));

        Map<Integer, Long> yearCount1 = new HashMap<>();
        yearCount1.put(2024, 1L);
        Map<Integer, Long> yearCount2 = new HashMap<>();
        yearCount2.put(2023, 1L);
        yearCount2.put(2022, 1L);
        Map<String, Map<Integer, Long>> expected = new HashMap<>();
        expected.put("Falcon", yearCount1);
        expected.put("Apollo", yearCount2);

        StepVerifier.create(taskService.getRocketsLaunchedByYear())
                .assertNext(output -> {
                    assertEquals(expected, output);
                })
                .verifyComplete();

        verify(spacexClientService, times(1)).getAllLaunches();
        verify(spacexClientService, times(3)).getRocketById(any());
        verifyNoMoreInteractions(spacexClientService);
    }

    @Test
    void testRocketLaunchedPerSite() {
        Launch launch = getLaunch(Instant.now(), "pad1", "rocket1");
        Rocket rocket = getRocket("rocket1", "Falcon");
        LaunchPad launchPad = getLaunchPad("pad1", "Site 1");

        given(spacexClientService.getAllLaunches()).willReturn(Flux.just(launch));
        given(spacexClientService.getLaunchPadById(launch.getLaunchPadId())).willReturn(Mono.just(launchPad));
        given(spacexClientService.getRocketById(launch.getRocketId())).willReturn(Mono.just(rocket));

        Map<String, Long> siteCount = new HashMap<>();
        siteCount.put("Site 1", 1L);
        Map<String, Map<String, Long>> expected = new HashMap<>();
        expected.put("Falcon", siteCount);

        StepVerifier.create(taskService.getLaunchesPerSite())
                .assertNext(output -> {
                    assertEquals(expected, output);
                })
                .verifyComplete();

        verify(spacexClientService, times(1)).getAllLaunches();
        verify(spacexClientService, times(1)).getRocketById(launch.getRocketId());
    }

    @Test
    void testRocketLaunchedPerSiteNullRocketAndLaunchPadName() {
        Launch launch = getLaunch(Instant.now(), "pad1", "rocket1");
        Rocket rocket = getRocket("rocket1", null);
        LaunchPad launchPad = getLaunchPad("pad1", null);

        given(spacexClientService.getAllLaunches()).willReturn(Flux.just(launch));
        given(spacexClientService.getLaunchPadById(launch.getLaunchPadId())).willReturn(Mono.just(launchPad));
        given(spacexClientService.getRocketById(launch.getRocketId())).willReturn(Mono.just(rocket));

        Map<String, Long> siteCount = new HashMap<>();
        siteCount.put("LAUNCHPAD_NAME_NA", 1L);
        Map<String, Map<String, Long>> expected = new HashMap<>();
        expected.put("ROCKET_NAME_NA", siteCount);

        StepVerifier.create(taskService.getLaunchesPerSite())
                .assertNext(output -> {
                    assertEquals(expected, output);
                })
                .verifyComplete();

        verify(spacexClientService, times(1)).getAllLaunches();
        verify(spacexClientService, times(1)).getRocketById(launch.getRocketId());
    }

    @Test
    void testRocketLaunchedPerSiteMultipleResult() {
        Launch launch1 = getLaunch(Instant.now(), "pad1", "rocket1");
        Launch launch2 = getLaunch(Instant.parse("2023-01-21T05:47:26.853Z"), "pad2", "rocket2");
        Launch launch3 = getLaunch(Instant.parse("2022-01-21T05:47:26.853Z"), "pad2", "rocket2");

        Rocket rocket1 = getRocket("rocket1", "Falcon");
        Rocket rocket2 = getRocket("rocket2", "Apollo");

        LaunchPad launchPad1 = getLaunchPad("pad1", "Site 1");
        LaunchPad launchPad2 = getLaunchPad("pad2", "Site 2");

        given(spacexClientService.getAllLaunches()).willReturn(Flux.just(launch1, launch2, launch3));
        given(spacexClientService.getRocketById("rocket1")).willReturn(Mono.just(rocket1));
        given(spacexClientService.getRocketById("rocket2")).willReturn(Mono.just(rocket2));
        given(spacexClientService.getLaunchPadById("pad1")).willReturn(Mono.just(launchPad1));
        given(spacexClientService.getLaunchPadById("pad2")).willReturn(Mono.just(launchPad2));

        Map<String, Long> siteCount1 = new HashMap<>();
        siteCount1.put("Site 1", 1L);
        Map<String, Long> siteCount2 = new HashMap<>();
        siteCount2.put("Site 2", 2L);
        Map<String, Map<String, Long>> expected = new HashMap<>();
        expected.put("Falcon", siteCount1);
        expected.put("Apollo", siteCount2);

        StepVerifier.create(taskService.getLaunchesPerSite())
                .assertNext(output -> {
                    assertEquals(expected, output);
                })
                .verifyComplete();

        verify(spacexClientService, times(1)).getAllLaunches();
        verify(spacexClientService, times(3)).getRocketById(any());
        verify(spacexClientService, times(3)).getLaunchPadById(any());
        verifyNoMoreInteractions(spacexClientService);
    }

    private static Launch getLaunch(Instant instant, String pad2, String rocket2) {
        return Launch.builder()
                .date(instant)
                .launchPadId(pad2)
                .rocketId(rocket2)
                .build();
    }

    private Rocket getRocket(String rocketId, String rocketName) {
        return Rocket.builder()
                .rocketId(rocketId)
                .rocketName(rocketName)
                .build();
    }

    private LaunchPad getLaunchPad(String id, String name) {
        return LaunchPad.builder()
                .launchPadName(name)
                .launchPadId(id)
                .build();
    }

}