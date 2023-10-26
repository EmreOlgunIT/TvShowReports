package com.example.tvshows.populate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/populate")
public class PopulateController {

    private final PopulateService populateService;

    @Autowired
    public PopulateController(PopulateService populateService) {
        this.populateService = populateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String populate() {
        populateService.populate();
        return "Populated";
    }

}