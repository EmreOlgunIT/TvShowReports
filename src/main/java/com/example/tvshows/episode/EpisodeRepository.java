package com.example.tvshows.episode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

    Optional<Episode> findTopByShowIdOrderByRatingDesc(int showId);

    @Query("SELECT e FROM Episode e JOIN FETCH e.show s " +
            "WHERE e.releaseUnixTime >= :startTimestamp " +
            "AND e.releaseUnixTime <= :endTimestamp")
    List<Episode> getEpisodesAiringBetweenUnixTimestamps(@Param("startTimestamp") long startTimestamp, @Param("endTimestamp") long endTimestamp);

}