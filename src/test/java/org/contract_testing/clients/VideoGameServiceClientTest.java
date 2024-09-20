package org.contract_testing.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.contract_testing.models.VideoGame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.lanwen.wiremock.ext.WiremockResolver;
import ru.lanwen.wiremock.ext.WiremockUriResolver;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@ExtendWith({ WiremockResolver.class, WiremockUriResolver.class })
class VideoGameServiceClientTest {
    @Autowired
    private VideoGameServiceClient videoGameServiceClient;

    @SneakyThrows
    @Test
    void fetchVideoGames(@WiremockResolver.Wiremock WireMockServer server, @WiremockUriResolver.WiremockUri String uri) {
        String videoGamesBody = FileUtils.readFileToString(new File("src/test/resources/videogames.json"), StandardCharsets.UTF_8);
        videoGameServiceClient.setBaseUrl(uri);
        server.stubFor(
                get(urlPathEqualTo("/api/v2/videogame"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(videoGamesBody)
                                .withHeader("Content-Type", "application/json"))
        );

        VideoGameServiceResponse response = videoGameServiceClient.fetchVideoGames();
        assertThat(response.getVideogames(), hasSize(10));
        assertThat(response.getVideogames().stream().map(VideoGame::getId).collect(Collectors.toSet()),
                is(equalTo(new HashSet<>(Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L)))));
    }

    @SneakyThrows
    @Test
    void getVideoGameById(@WiremockResolver.Wiremock WireMockServer server, @WiremockUriResolver.WiremockUri String uri) {
        String videoGameBody = FileUtils.readFileToString(new File("src/test/resources/videogame_10L.json"), StandardCharsets.UTF_8);
        videoGameServiceClient.setBaseUrl(uri);
        server.stubFor(
                get(urlPathEqualTo("/api/v2/videogame/10"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(videoGameBody)
                                .withHeader("Content-Type", "application/json"))
        );

        VideoGame videoGame = videoGameServiceClient.fetchVideoGameById(10);
        assertThat(videoGame, is(equalTo(new VideoGame("Driving", 10L, "Grand Theft Auto III", "Mature", "2001-04-23 23:59:59",90L))));
    }
}