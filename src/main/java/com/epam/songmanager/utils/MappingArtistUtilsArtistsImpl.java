package com.epam.songmanager.utils;

import com.epam.songmanager.model.Artist;
import com.epam.songmanager.model.Genre;
import com.epam.songmanager.model.dto.ArtistDto;
import com.epam.songmanager.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MappingArtistUtilsArtistsImpl implements MappingUtilsArtists {

    @Autowired
    private GenreRepository genreRepository;

    public ArtistDto mapToDto(Artist entity){
        ArtistDto dto = new ArtistDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setNotes(entity.getNotes());
        for (Genre g: entity.getGenres()) {
            dto.getGenres().add(g.getId());
        }
        return dto;
    }

    public Artist mapToEntity(ArtistDto dto){
        Artist entity = new Artist();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setNotes(dto.getNotes());
        for (Long id: dto.getGenres()) {
            entity.getGenres().add(genreRepository.getById(id));
        }
        return entity;
    }

}