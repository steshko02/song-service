package com.epam.songmanager.repository;

import com.epam.songmanager.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository  extends JpaRepository<Album,Long> {
}
