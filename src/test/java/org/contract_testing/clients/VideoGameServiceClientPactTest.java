package org.contract_testing.clients;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.contract_testing.models.VideoGame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "VideoGameService")
public class VideoGameServiceClientPactTest {
    @Autowired
    private VideoGameServiceClient videoGameServiceClient;

    @Pact(consumer = "VideoGamesCatalogue")
    public RequestResponsePact allVideoGames(PactDslWithProvider builder) {
        return builder
                .given("video games exists")
                .uponReceiving("get all video games")
                .path("/api/v2/videogame")
                .willRespondWith()
                .status(200)
                .body(
                        new PactDslJsonBody()
                                .minArrayLike("videogames",1,10)
                                .integerType("id",1L)
                                .stringType("name","Resident Evil 4")
                                .stringType("releaseDate","2005-10-01 23:59:59")
                                .integerType("reviewScore",85L)
                                .stringType("category","Shooter")
                                .stringType("rating","Universal")
                                .closeObject()
                                .closeArray()
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "allVideoGames", pactVersion = PactSpecVersion.V3)
    void testAllProducts(MockServer mockServer) {
        videoGameServiceClient.setBaseUrl(mockServer.getUrl());
        List<VideoGame> videoGames = videoGameServiceClient.fetchVideoGames().getVideogames();
        assertThat(videoGames, hasSize(10));
        assertThat(videoGames.get(0), is(equalTo(new VideoGame("Shooter",1L, "Resident Evil 4", "Universal","2005-10-01 23:59:59",85L))));
    }



    @Pact(consumer = "VideoGamesCatalogue")
    public RequestResponsePact singleVideoGame(PactDslWithProvider builder) {
        return builder
                .given("video game with ID 10 exists","id",10)
                .uponReceiving("get video game with ID 10")
                .path("/api/v2/videogame/10")
                .willRespondWith()
                .status(200)
                .body(
                        new PactDslJsonBody()
                                .integerType("id",10L)
                                .stringType("name","Grand Theft Auto III")
                                .stringType("releaseDate","2001-04-23 23:59:59")
                                .integerType("reviewScore",90L)
                                .stringType("category","Driving")
                                .stringType("rating","Mature")
                )
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "singleVideoGame", pactVersion = PactSpecVersion.V3)
    void testSingleProduct(MockServer mockServer) {
        videoGameServiceClient.setBaseUrl(mockServer.getUrl());
        VideoGame videoGame = videoGameServiceClient.fetchVideoGameById(10L);
        assertThat(videoGame, is(equalTo(new VideoGame("Driving",10L,"Grand Theft Auto III","Mature","2001-04-23 23:59:59",90L))));
    }
}
