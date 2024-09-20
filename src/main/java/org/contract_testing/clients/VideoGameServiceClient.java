package org.contract_testing.clients;

import org.contract_testing.models.VideoGame;
import org.contract_testing.models.VideoGamesCatalogue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VideoGameServiceClient {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${serviceClients.videoGames.baseUrl}")
    private String baseUrl;

    public VideoGameServiceResponse fetchVideoGames() {
        return restTemplate.getForObject(baseUrl + "/api/v2/videogame", VideoGameServiceResponse.class);
    }

    public VideoGame fetchVideoGameById(long id) {
        return restTemplate.getForObject(baseUrl + "/api/v2/videogame/" + id, VideoGame.class);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
