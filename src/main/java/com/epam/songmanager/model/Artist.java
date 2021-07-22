package com.epam.songmanager.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  String name;

    private  String notes;

    @ManyToMany
    @JoinTable(name="artist_genre", joinColumns=@JoinColumn(name="artist_id"),
            inverseJoinColumns=@JoinColumn(name="genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(mappedBy="artists")
    private Set<Album> albums = new HashSet<>();

    public Artist() {
    }
}