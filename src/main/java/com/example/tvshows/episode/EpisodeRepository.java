package com.example.tvshows.episode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {

    Optional<Episode> findTopByShowIdOrderByRatingDesc(int showId);

}