package com.example.tvshows.report;

import com.example.tvshows.show.Show;
import com.example.tvshows.show.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ShowRepository showRepository;

    @Autowired
    public ReportService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public void createTop10RatedShowsReport(String filename) {
        Optional<List<Show>> top10RatedShowsOptional = showRepository.getTop10RatedShows();

        if (top10RatedShowsOptional.isPresent()) {

            List<Show> showList = top10RatedShowsOptional.get();

            try {
                FileWriter fileWriter = new FileWriter("src/main/resources/"+filename+".txt");

                for (Show s : showList) {
                    fileWriter.write(s.getRating()+";"+s.getName());
                    fileWriter.write(System.lineSeparator());
                }
                fileWriter.close();
            } catch (IOException e) {}

        }
    }

}