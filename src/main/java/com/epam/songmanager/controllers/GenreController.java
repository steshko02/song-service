package com.epam.songmanager.controllers;

import com.epam.songmanager.exceptions.EntityNotFoundException;
import com.epam.songmanager.model.entity.Genre;
import com.epam.songmanager.service.interfaces.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> getGenres() throws EntityNotFoundException {
           return genreService.getAll();
    }

    @DeleteMapping("/genres")
    public Long[] delete(@RequestParam Long[] ids) throws EntityNotFoundException{
        return  genreService.delete(ids);
    }

    @PostMapping("/genres/add")
    public  Long add(@RequestBody Genre genre) throws EntityNotFoundException{
        return   genreService.add(genre);
    }
}
