package com.epam.songmanager.controllers;

import com.epam.songmanager.model.Genre;
import com.epam.songmanager.model.dto.ArtistDto;
import com.epam.songmanager.service.ArtistService;
import com.epam.songmanager.utils.MappingArtistUtilsArtistsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ArtistController {

    @Autowired
    private ArtistService artistService;
    @Autowired
    private MappingArtistUtilsArtistsImpl mappingUtils;

    @PostMapping("/artists")
    public  Long add(@RequestBody ArtistDto artistDto){
        return artistService.add(mappingUtils.mapToEntity(artistDto));
    }

    @PutMapping("/artists/{id}")
    public  Long edit(@RequestBody ArtistDto artistDto,@PathVariable  Long id){
        return artistService.edit( mappingUtils.mapToEntity(artistDto), id);
    }

    @GetMapping("/artists/{id}")
    public ArtistDto getGenres(@PathVariable Long id) {
        return mappingUtils.mapToDto(artistService.get(id));
    }

    @DeleteMapping("/artists")
    public Long[] delete(@RequestParam Long[] ids){
        return  artistService.delete(ids);
    }

    @GetMapping("/artists")
    public List<Genre> getByFilters(@RequestParam String name, @RequestParam Long[] id) {

        artistService.getByFilters(name,id);

        return null;
    }
}
