package com.epam.songmanager.repository;

import com.epam.songmanager.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song,Long> {
}
