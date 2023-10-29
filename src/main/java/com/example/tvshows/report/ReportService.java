package com.example.tvshows.report;

import com.example.tvshows.episode.Episode;
import com.example.tvshows.episode.EpisodeService;
import com.example.tvshows.genre.Genre;
import com.example.tvshows.show.Show;
import com.example.tvshows.show.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class ReportService {

    private final ShowService showService;
    private final EpisodeService episodeService;

    @Autowired
    public ReportService(ShowService showService, EpisodeService episodeService) {
        this.showService = showService;
        this.episodeService = episodeService;
    }

    public void createTop10RatedShowsReport(String filename) {
        List<Show> showList = showService.getTop10RatedShows();

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/" + filename + ".txt");

            for (Show s : showList) {
                fileWriter.write(s.getRating() + ";" + s.getName());
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.close();
        } catch (IOException e) {
        }

    }

    public void createSummaryReport(String filename) {
        List<Show> showList = showService.getAllShowsIncludingGenres();

        HashMap<Object, Object> amountOfEpisodesMap = showService.getAmountOfEpisodesPerShowMap();
        HashMap<Object, Object> amountOfReleasedEpisodesMap = showService.getAmountOfReleasedEpisodesPerShowMap();

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/" + filename + ".txt");

            for (Show s : showList) {
                fileWriter.write(s.getName() + ";" + this.createGenreNamesString(s) + ";" + amountOfEpisodesMap.get(s.getId()) + ";" + amountOfReleasedEpisodesMap.get(s.getId()));
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.close();
        } catch (IOException e) {
        }

    }

    public void createBestEpisodePerShowReport(String filename) {

        List<Show> shows = showService.getAllShowsIncludingGenres();
        HashMap<Integer, Episode> bestEpisodesPerShowMap = episodeService.getBestEpisodePerShowMap(shows);

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/" + filename + ".txt");

            for (Show s : shows) {
                String bestEpisodeString = "N/A;N/A;N/A;N/A;";

                if (bestEpisodesPerShowMap.containsKey(s.getId())) {
                    Episode e = bestEpisodesPerShowMap.get(s.getId());
                    if (e != null) {
                        bestEpisodeString = e.getSeasonNumber() + ";" + e.getEpisodeNumber() + ";" + e.getName() + ";" + e.getRating();
                    }
                }

                fileWriter.write(s.getName() + ";" + s.getNetwork() + ";" + this.createGenreNamesString(s) + ";" + bestEpisodeString);
                fileWriter.write(System.lineSeparator());
            }

            fileWriter.close();
        } catch (IOException e) {}

    }

    public void createRecommendedShowReport(String filename, String genre) {
        Show s = showService.getTopRatedShowByGenre(genre);

        String imdbUrl = "https://www.imdb.com/title/"+s.getImdbUrlId();

        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/" + filename + ".txt");
            fileWriter.write(s.getName() + ";" + s.getRating() + ";" + this.createGenreNamesString(s) + ";" + s.getSummary() + ";" + imdbUrl);
            fileWriter.close();
        } catch (IOException e) {}

    }

    private String createGenreNamesString(Show s) {
        Set<Genre> genres = s.getGenres();
        StringBuilder genreNames = new StringBuilder();

        for (Genre genre : genres) {
            if (genreNames.length() > 0) {
                genreNames.append(",");
            }
            genreNames.append(genre.getName());
        }

        return genreNames.toString();
    }

}