package com.example.tvshows.report;

import com.example.tvshows.genre.Genre;
import com.example.tvshows.show.Show;
import com.example.tvshows.show.ShowRepository;
import com.example.tvshows.show.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class ReportService {

    private final ShowRepository showRepository;
    private final ShowService showService;

    @Autowired
    public ReportService(ShowRepository showRepository, ShowService showService) {
        this.showRepository = showRepository;
        this.showService = showService;
    }

    public void createTop10RatedShowsReport(String filename) {
        List<Show> showList = showService.getTop10RatedShows();

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/"+filename+".txt");

            for (Show s : showList) {
                fileWriter.write(s.getRating()+";"+s.getName());
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.close();
        } catch (IOException e) {}

    }

    public void createSummaryReport(String filename) {
        List<Show> showList = showService.getAllShowsIncludingGenres();

        HashMap<Object, Object> amountOfEpisodesMap = showService.getAmountOfEpisodesPerShowMap();
        HashMap<Object, Object> amountOfReleasedEpisodesMap = showService.getAmountOfReleasedEpisodesPerShowMap();

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/"+filename+".txt");

            for (Show s : showList) {

                Set<Genre> genres = s.getGenres();
                StringBuilder genreNames = new StringBuilder();

                for (Genre genre : genres) {
                    if (genreNames.length() > 0) {
                        genreNames.append(",");
                    }
                    genreNames.append(genre.getName());
                }

                fileWriter.write(s.getName()+";"+genreNames+";"+amountOfEpisodesMap.get(s.getId())+";"+amountOfReleasedEpisodesMap.get(s.getId()));
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.close();
        } catch (IOException e) {}

    }

}