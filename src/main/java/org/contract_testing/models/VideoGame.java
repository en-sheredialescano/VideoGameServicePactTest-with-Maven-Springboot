package org.contract_testing.models;

import lombok.Data;

@Data
public class VideoGame {
    private final String category;
    private final Long id;
    private final String name;
    private final String rating;
    private final String releaseDate;
    private final Long reviewScore;
}