package com.example.tvshows.show;

import com.example.tvshows.episode.Episode;
import com.example.tvshows.genre.Genre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor //Generates a constructor with all arguments
@NoArgsConstructor //Generates a constructor with no arguments
@Data //Generates getters and setters
@Entity
@Table
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String summary;
    private String network;
    private double rating;
    private String imdbUrlId;

    @OneToMany(mappedBy = "show")
    private List<Episode> episodes;

    @ManyToMany
    @JoinTable(
            name = "show_genre",
            joinColumns = @JoinColumn(name = "show_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    public Show(String name, String summary, String network, double rating, Set<Genre> genres, String imdbUrlId) {
        this.name = name;
        this.summary = summary;
        this.network = network;
        this.rating = rating;
        this.genres = genres;
        this.imdbUrlId = imdbUrlId;
    }

}