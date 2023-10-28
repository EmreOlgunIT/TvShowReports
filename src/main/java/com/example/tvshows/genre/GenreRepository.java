package com.example.tvshows.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    @Query("SELECT g FROM Genre g WHERE g.name IN :genreNames")
    List<Genre> getGenresByNames(@Param("genreNames") List<String> genreNames);

}
