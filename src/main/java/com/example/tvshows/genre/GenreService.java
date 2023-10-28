package com.example.tvshows.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public HashMap<String, Genre> getGenreHashMapByNames(List<String> genreNames, boolean createGenreIfMissing) {

        List<Genre> foundGenres = genreRepository.getGenresByNames(genreNames);
        HashMap<String, Genre> genreMap = new HashMap<>();

        if (!createGenreIfMissing) {
            for (Genre foundGenre : foundGenres) {
                genreMap.put(foundGenre.getName(), foundGenre);
            }
        } else {
            for (String genreName : genreNames) {
                boolean genreFound = false;
                for (Genre foundGenre : foundGenres) {
                    if (foundGenre.getName().equalsIgnoreCase(genreName)) {
                        genreMap.put(genreName, foundGenre);
                        genreFound = true;
                        break;
                    }
                }

                if (!genreFound) {
                    Genre newGenre = new Genre();
                    newGenre.setName(genreName);

                    genreRepository.save(newGenre);
                    genreMap.put(genreName, newGenre);
                }
            }
        }

        return genreMap;
    }

}
