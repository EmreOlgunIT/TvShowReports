package com.example.tvshows.report;

import com.example.tvshows.episode.Episode;
import com.example.tvshows.episode.EpisodeService;
import com.example.tvshows.genre.Genre;
import com.example.tvshows.network.Network;
import com.example.tvshows.show.Show;
import com.example.tvshows.show.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

    public byte[] createTop10RatedShowsReport() {
        List<Show> showList = showService.getTop10RatedShows();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            for (Show s : showList) {
                String string = s.getRating() + ";" + s.getName() + System.lineSeparator();
                byteArrayOutputStream.write(string.getBytes());
            }
        } catch (IOException e) {}

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] createSummaryReport() {
        List<Show> showList = showService.getAllShowsIncludingGenresAndNetwork();

        HashMap<Object, Object> amountOfEpisodesMap = showService.getAmountOfEpisodesPerShowMap();
        HashMap<Object, Object> amountOfReleasedEpisodesMap = showService.getAmountOfReleasedEpisodesPerShowMap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            for (Show s : showList) {

                String networkName = "";
                if (s.getNetwork() != null) {
                    networkName = s.getNetwork().getName();
                }

                String string = s.getName() + ";" + networkName + ";" + this.createGenreNamesString(s) + ";" + amountOfEpisodesMap.get(s.getId()) + ";" + amountOfReleasedEpisodesMap.get(s.getId()) + System.lineSeparator();
                byteArrayOutputStream.write(string.getBytes());
            }
        } catch (IOException e) {}

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] createBestEpisodePerShowReport() {

        List<Show> shows = showService.getAllShowsIncludingGenresAndNetwork();
        HashMap<Integer, Episode> bestEpisodesPerShowMap = episodeService.getBestEpisodePerShowMap(shows);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {

            for (Show s : shows) {
                String bestEpisodeString = "N/A;N/A;N/A;N/A;";

                if (bestEpisodesPerShowMap.containsKey(s.getId())) {
                    Episode e = bestEpisodesPerShowMap.get(s.getId());
                    if (e != null) {
                        bestEpisodeString = e.getSeasonNumber() + ";" + e.getEpisodeNumber() + ";" + e.getName() + ";" + e.getRating();
                    }
                }

                String string = s.getName() + ";" + s.getNetwork().getName() + ";" + this.createGenreNamesString(s) + ";" + bestEpisodeString + System.lineSeparator();
                byteArrayOutputStream.write(string.getBytes());
            }

        } catch (IOException e) {}

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] createRecommendedShowReport(String genre) {
        Show s = showService.getTopRatedShowByGenre(genre);

        String imdbUrl = "https://www.imdb.com/title/"+s.getImdbUrlId();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            String string = s.getName() + ";" + s.getRating() + ";" + this.createGenreNamesString(s) + ";" + s.getSummary() + ";" + imdbUrl;
            byteArrayOutputStream.write(string.getBytes());
        } catch (IOException e) {}

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] createNextWeekReport() {
        List<Episode> episodesAiringNextWeek = episodeService.getEpisodesAiringNextWeek();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {

            for (Episode e : episodesAiringNextWeek) {
                String episodeAiringDayString = this.createEpisodeAiringDayString(e) + System.lineSeparator();
                byteArrayOutputStream.write(episodeAiringDayString.getBytes());
            }

        } catch (IOException e) {}

        return byteArrayOutputStream.toByteArray();
    }


    public byte[] createTopNetworkReport(){

        List<Object[]> top10NetworksByAverageRatingList = showService.getTop10NetworksByAverageRating();
        List<Integer> networkIds = new  ArrayList<>();

        HashMap<Object, Object> showCountPerNetworkMap = showService.getShowCountPerNetworkMap();

        HashMap<Integer, Double> averageRatingMap = new HashMap<>();
        HashMap<Integer, Network> networksMap = new HashMap<>();

        for (Object[] objArray :top10NetworksByAverageRatingList) {
            Network network = (Network) objArray[0];
            double averageRating = (double) objArray[1];

            averageRatingMap.put(network.getId(), averageRating);
            networksMap.put(network.getId(), network);
            networkIds.add(network.getId());
        }

        HashMap<Integer, Show> getBestShowByNetworkMap = showService.getBestShowByNetworkMap(networkIds);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            for (Map.Entry<Integer, Show> entry : getBestShowByNetworkMap.entrySet()) {
                int networkId = entry.getKey();
                Show show = entry.getValue();
                String s = averageRatingMap.get(networkId) + ";" + networksMap.get(networkId).getName() + ";" + show.getName() + ";" + show.getRating() + ";" + showCountPerNetworkMap.get(networkId);
                byteArrayOutputStream.write(s.getBytes());
            }
        } catch (IOException e) {}

        return byteArrayOutputStream.toByteArray();

    }

    private String createEpisodeAiringDayString(Episode e){
        LocalDate date = Instant.ofEpochSecond(e.getReleaseUnixTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        StringBuilder sb = new StringBuilder();
        sb.append(e.getShow().getName()+";");

        for (int i = 1; i < 8; i++) {
            if (i == dayOfWeek.getValue()) {
                sb.append("S"+e.getSeasonNumber()+"E"+e.getEpisodeNumber());
            } else {
                sb.append(";");
            }
        }

        return sb.toString();
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