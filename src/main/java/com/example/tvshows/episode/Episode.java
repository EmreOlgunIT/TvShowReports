package com.example.tvshows.episode;

import com.example.tvshows.show.Show;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor //Generates a constructor with all arguments
@NoArgsConstructor //Generates a constructor with no arguments
@Data //Generates getters and setters
@Entity
@Table
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private int seasonNumber;
    private int episodeNumber;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String summary;
    private double rating;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    public Episode(int seasonNumber, int episodeNumber, String name, String summary, double rating, Show show) {
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.summary = summary;
        this.rating = rating;
        this.show = show;
    }

}