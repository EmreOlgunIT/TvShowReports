package com.example.tvshows.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

    @Query("SELECT s FROM Show s ORDER BY s.rating DESC LIMIT 10")
    List<Show> getTop10RatedShows();

    @Query("SELECT s FROM Show s LEFT JOIN FETCH s.genres")
    List<Show> getAllShowsIncludingGenres();

    @Query("SELECT s.id, COUNT(e) FROM Show s JOIN s.episodes e GROUP BY s.id")
    List<Object[]> getAmountOfEpisodesPerShow();

    @Query("SELECT s.id, COUNT(e) FROM Show s JOIN s.episodes e WHERE e.releaseUnixTime < :unixTimestamp GROUP BY s.id")
    List<Object[]> getAmountOfReleasedEpisodesPerShowBeforeUnixTimestamp(@Param("unixTimestamp") long unixTimestamp);
}