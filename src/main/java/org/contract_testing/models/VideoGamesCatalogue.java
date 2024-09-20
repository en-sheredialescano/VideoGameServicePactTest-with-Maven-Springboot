package org.contract_testing.models;

import lombok.Data;

import java.util.List;

@Data
public class VideoGamesCatalogue {
    private final String name;
    private final List<VideoGame> videogames;
}
