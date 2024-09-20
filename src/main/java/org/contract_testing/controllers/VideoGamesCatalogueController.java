package org.contract_testing.controllers;

import org.contract_testing.clients.VideoGameServiceClient;
import org.contract_testing.models.VideoGamesCatalogue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VideoGamesCatalogueController {
    @Autowired
    private VideoGameServiceClient videoGameServiceClient;

    @GetMapping("/catalogue")
    public String catalogue(Model model) {
        VideoGamesCatalogue catalogue = new VideoGamesCatalogue("Default Catalogue", videoGameServiceClient.fetchVideoGames().getVideogames());
        model.addAttribute("catalogue", catalogue);
        return "catalogue";
    }

    @GetMapping("/catalogue/{id}")
    public String catalogue(@PathVariable("id") Long id, Model model) {
        model.addAttribute("videoGame", videoGameServiceClient.fetchVideoGameById(id));
        return "details";
    }
}
