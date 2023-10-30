package com.example.tvshows.show;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;

    @Autowired
    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public List<Show> getTop10RatedShows() {
        return showRepository.getTop10RatedShows();
    }

    public List<Show> getAllShowsIncludingGenresAndNetwork() {
        return showRepository.getAllShowsIncludingGenresAndNetwork();
    }

    public HashMap<Object, Object> getAmountOfEpisodesPerShowMap() {
        HashMap<Object, Object> amountOfEpisodesMap = new HashMap<>();
        List<Object[]> amountOfEpisodesPerShow = showRepository.getAmountOfEpisodesPerShow();

        for (Object[] objArray : amountOfEpisodesPerShow) {
            amountOfEpisodesMap.put(objArray[0], objArray[1]);
        }

        return amountOfEpisodesMap;
    }

    public HashMap<Object, Object> getAmountOfReleasedEpisodesPerShowMap() {
        Instant currentTimestamp = Instant.now();
        long currentUnixTimestamp = currentTimestamp.getEpochSecond();
        List<Object[]> amountOfReleasedEpisodesPerShow = showRepository.getAmountOfReleasedEpisodesPerShowBeforeUnixTimestamp(currentUnixTimestamp);

        HashMap<Object, Object> amountOfReleasedEpisodesPerShowMap = new HashMap<>();

        for (Object[] objArray : amountOfReleasedEpisodesPerShow) {
            amountOfReleasedEpisodesPerShowMap.put(objArray[0], objArray[1]);
        }

        return amountOfReleasedEpisodesPerShowMap;
    }

    public Show getTopRatedShowByGenre(String genre) {
        return showRepository.getTopRatedShowByGenre(genre);
    }

}
