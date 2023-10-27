package com.example.tvshows.populate;

import com.example.tvshows.episode.Episode;
import com.example.tvshows.episode.EpisodeRepository;
import com.example.tvshows.show.Show;
import com.example.tvshows.show.ShowRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
public class PopulateService {

    private final ShowRepository showRepository;
    private final EpisodeRepository episodeRepository;

    @Autowired
    public PopulateService(ShowRepository showRepository, EpisodeRepository episodeRepository) {
        this.showRepository = showRepository;
        this.episodeRepository = episodeRepository;
    }

    public void populate() {

        HttpClient httpClient = HttpClient.newBuilder().build();
        String showSearchApiUrl = "https://api.tvmaze.com/singlesearch/shows?q=";
        String episodeSearchApiUrl = "https://api.tvmaze.com/shows/";

        ArrayList<String> tvShowNames = this.getTvShowNamesFromTxtFile();

        for (int i = 0; i < tvShowNames.size(); i++) {

            HttpRequest showSearchRequest = HttpRequest.newBuilder()
                    .uri(URI.create(showSearchApiUrl+tvShowNames.get(i).replaceAll(" ", "%20"))).GET().build();

            try {
                HttpResponse<String> showSearchResponse = httpClient.send(showSearchRequest, HttpResponse.BodyHandlers.ofString());

                if (showSearchResponse.statusCode() == 429) {
                    Thread.sleep(2000);
                    i--;
                    continue;
                } else if (showSearchResponse.statusCode() == 200) {

                    JSONObject showSearchResponseBody = new JSONObject(showSearchResponse.body());
                    Show savedShow = showRepository.save(this.createShowObjectFromResponseBody(showSearchResponseBody));

                    HttpRequest episodeSearchRequest = HttpRequest.newBuilder()
                            .uri(URI.create(episodeSearchApiUrl+showSearchResponseBody.getInt("id")+"/episodes")).GET().build();

                    HttpResponse<String> episodeSearchResponse = httpClient.send(episodeSearchRequest, HttpResponse.BodyHandlers.ofString());

                    if (episodeSearchResponse.statusCode() == 200) {
                        ArrayList<Episode> episodes = this.createEpisodesObjectArrayFromResponseBody(episodeSearchResponse.body(), savedShow);
                        episodeRepository.saveAll(episodes);
                    }

                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private Show createShowObjectFromResponseBody(JSONObject body) {

        String network = "";
        if (!body.isNull("network")) {
            network = body.getJSONObject("network").getString("name");
        }

        double rating = 0;
        if (!body.getJSONObject("rating").isNull("average")) {
            rating = body.getJSONObject("rating").getDouble("average");
        }

        Show show = new Show(
                body.getString("name"),
                body.getString("summary"),
                network,
                rating
        );

        return show;
    }

    private ArrayList<Episode> createEpisodesObjectArrayFromResponseBody(String responseBody, Show show) {
        ArrayList<Episode> episodesObjectArray = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(responseBody);

        for (Object object : jsonArray) {

            JSONObject jsonObject = (JSONObject)object;

            double rating = 0;
            if (!jsonObject.getJSONObject("rating").isNull("average")) {
                rating = jsonObject.getJSONObject("rating").getDouble("average");
            }

            String summary = "";
            if (!jsonObject.isNull("summary")) {
                summary = jsonObject.getString("summary");
            }

            episodesObjectArray.add(new Episode(
                    jsonObject.getInt("season"),
                    jsonObject.getInt("number"),
                    jsonObject.getString("name"),
                    summary,
                    rating,
                    show
            ));
        }

        return episodesObjectArray;
    }

    private ArrayList<String> getTvShowNamesFromTxtFile() {
        ArrayList<String> tvShowNames = new ArrayList<>();

        ClassLoader classLoader = PopulateService.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("shows.txt");

        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    tvShowNames.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tvShowNames;
    }

}