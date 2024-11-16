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

        System.out.println(Instant.now().atZone(ZoneId.of("UTC")).getYear());

        Launch launch = Launch.builder()
                .date(Instant.now())
                .launchPadId("pad1")
                .rocketId("rocket1")
                .build();

        Rocket rocket = Rocket.builder()
                .rocketId("rocket1")
                .rocketName("falcon")
                .build();

        given(spacexClientService.getAllLaunches()).willReturn(Flux.just(launch));
        given(spacexClientService.getRocketById(launch.getRocketId())).willReturn(Mono.just(rocket));

        Map<Integer, Long> yearCount = new HashMap<>();
        yearCount.put(2024, 1L);
        Map<String, Map<Integer, Long>> expected = new HashMap<>();
        expected.put("falcon", yearCount);

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

        System.out.println(Instant.now().atZone(ZoneId.of("UTC")).getYear());

        Launch launch = Launch.builder()
                .date(Instant.now())
                .launchPadId("pad1")
                .rocketId("rocket1")
                .build();

        Rocket rocket = Rocket.builder()
                .rocketId("rocket1")
                .build();

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

        Launch launch1 = Launch.builder()
                .date(Instant.now())
                .launchPadId("pad1")
                .rocketId("rocket1")
                .build();

        Launch launch2 = Launch.builder()
                .date(Instant.parse("2023-01-21T05:47:26.853Z"))
                .launchPadId("pad2")
                .rocketId("rocket2")
                .build();

        Launch launch3 = Launch.builder()
                .date(Instant.parse("2022-01-21T05:47:26.853Z"))
                .launchPadId("pad2")
                .rocketId("rocket2")
                .build();

        Rocket rocket1 = Rocket.builder()
                .rocketId("rocket1")
                .rocketName("Falcon")
                .build();
        Rocket rocket2 = Rocket.builder()
                .rocketId("rocket2")
                .rocketName("Apollo")
                .build();

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

        System.out.println(Instant.now().atZone(ZoneId.of("UTC")).getYear());

        Launch launch = Launch.builder()
                .date(Instant.now())
                .launchPadId("pad1")
                .rocketId("rocket1")
                .build();

        LaunchPad launchPad = LaunchPad.builder()
                .launchPadName("Site 1")
                .launchPadId("pad1")
                .build();

        Rocket rocket = Rocket.builder()
                .rocketId("rocket1")
                .rocketName("falcon")
                .build();

        given(spacexClientService.getAllLaunches()).willReturn(Flux.just(launch));
        given(spacexClientService.getLaunchPadById(launch.getLaunchPadId())).willReturn(Mono.just(launchPad));
        given(spacexClientService.getRocketById(launch.getRocketId())).willReturn(Mono.just(rocket));

        Map<String, Long> siteCount = new HashMap<>();
        siteCount.put("Site 1", 1L);
        Map<String, Map<String, Long>> expected = new HashMap<>();
        expected.put("falcon", siteCount);

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

        System.out.println(Instant.now().atZone(ZoneId.of("UTC")).getYear());

        Launch launch = Launch.builder()
                .date(Instant.now())
                .launchPadId("pad1")
                .rocketId("rocket1")
                .build();

        LaunchPad launchPad = LaunchPad.builder()
                .launchPadId("pad1")
                .build();

        Rocket rocket = Rocket.builder()
                .rocketId("rocket1")
                .build();

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

        System.out.println(Instant.now().atZone(ZoneId.of("UTC")).getYear());

        Launch launch1 = Launch.builder()
                .date(Instant.now())
                .launchPadId("pad1")
                .rocketId("rocket1")
                .build();

        Launch launch2 = Launch.builder()
                .date(Instant.parse("2023-01-21T05:47:26.853Z"))
                .launchPadId("pad2")
                .rocketId("rocket2")
                .build();

        Launch launch3 = Launch.builder()
                .date(Instant.parse("2022-01-21T05:47:26.853Z"))
                .launchPadId("pad2")
                .rocketId("rocket2")
                .build();

        Rocket rocket1 = Rocket.builder()
                .rocketId("rocket1")
                .rocketName("Falcon")
                .build();
        Rocket rocket2 = Rocket.builder()
                .rocketId("rocket2")
                .rocketName("Apollo")
                .build();

        LaunchPad launchPad1 = LaunchPad.builder()
                .launchPadName("Site 1")
                .launchPadId("pad1")
                .build();
        LaunchPad launchPad2 = LaunchPad.builder()
                .launchPadName("Site 2")
                .launchPadId("pad2")
                .build();

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

}