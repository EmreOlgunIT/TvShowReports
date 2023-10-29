package com.example.tvshows.episode;

import com.example.tvshows.show.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class EpisodeService {

    private final EpisodeRepository episodeRepository;

    @Autowired
    public EpisodeService(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    public HashMap<Integer, Episode> getBestEpisodePerShowMap(List<Show> shows) {

        HashMap<Integer, Episode> bestEpisodePerShowMap = new HashMap<>();

        for (Show s: shows) {

            Optional<Episode> episodeOptional =  episodeRepository.findTopByShowIdOrderByRatingDesc(s.getId());

            if (episodeOptional.isPresent()) {
                bestEpisodePerShowMap.put(s.getId(), episodeOptional.get());
            }


        }

        return bestEpisodePerShowMap;
    }

}
