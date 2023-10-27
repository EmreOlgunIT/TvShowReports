package com.example.tvshows.show;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Integer> {

    @Query("SELECT s FROM Show s ORDER BY s.rating DESC LIMIT 10")
    Optional<List<Show>> getTop10RatedShows();

}