package com.example.tvshows.show;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public Show(String name, String summary, String network, double rating) {
        this.name = name;
        this.summary = summary;
        this.network = network;
        this.rating = rating;
    }

}