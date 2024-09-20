package org.contract_testing.clients;

import lombok.Data;
import org.contract_testing.models.VideoGame;

import java.util.List;

@Data
public class VideoGameServiceResponse {
    private List<VideoGame> videogames;
}
