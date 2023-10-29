package com.example.tvshows.episode;

import com.example.tvshows.show.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
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

    public List<Episode> getEpisodesAiringNextWeek() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfNextWeek = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfNextWeek = startOfNextWeek.plusDays(7).withHour(23).withMinute(59).withSecond(59);

        return episodeRepository.getEpisodesAiringBetweenUnixTimestamps(startOfNextWeek.toEpochSecond(ZoneOffset.UTC), endOfNextWeek.toEpochSecond(ZoneOffset.UTC));
    }

}
