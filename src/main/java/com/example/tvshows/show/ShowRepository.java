package com.example.tvshows.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

    @Query("SELECT s FROM Show s ORDER BY s.rating DESC LIMIT 10")
    List<Show> getTop10RatedShows();

    @Query("SELECT s FROM Show s LEFT JOIN FETCH s.genres LEFT JOIN FETCH s.network")
    List<Show> getAllShowsIncludingGenresAndNetwork();

    @Query("SELECT s.id, COUNT(e) FROM Show s JOIN s.episodes e GROUP BY s.id")
    List<Object[]> getAmountOfEpisodesPerShow();

    @Query("SELECT s.id, COUNT(e) FROM Show s JOIN s.episodes e WHERE e.releaseUnixTime < :unixTimestamp GROUP BY s.id")
    List<Object[]> getAmountOfReleasedEpisodesPerShowBeforeUnixTimestamp(@Param("unixTimestamp") long unixTimestamp);

    @Query("SELECT s FROM Show s JOIN s.genres g WHERE g.name = :genre ORDER BY s.rating DESC LIMIT 1")
    Show getTopRatedShowByGenre(@Param("genre") String genre);


    @Query("SELECT s.network, AVG(s.rating) AS avgRating " +
            "FROM Show s " +
            "GROUP BY s.network " +
            "ORDER BY avgRating DESC " +
            "LIMIT 10"
    )
    List<Object[]> getTop10NetworksByAverageRating();

    @Query("SELECT s FROM Show s " +
            "WHERE s.network.id = :networkId " +
            "AND s.rating = (SELECT MAX(s2.rating) FROM Show s2 WHERE s2.network = s.network) " +
            "ORDER BY s.network.id")
    Show getBestShowsByNetworkId(@Param("networkId") Integer networkIds);

    @Query("SELECT s.network.id, COUNT(s)" +
            "FROM Show s " +
            "GROUP BY s.network.id")
    List<Object[]> countShowsByNetwork();

}