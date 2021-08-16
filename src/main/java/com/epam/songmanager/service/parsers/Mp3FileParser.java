package com.epam.songmanager.service.parsers;

import lombok.Data;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Data
public class Mp3FileParser implements AudioParser {
    private  AbstractID3v2 tags;

    public void create(File file) throws IOException, TagException {
        MP3File mp3File  = new MP3File(file);
        if(mp3File.hasID3v2Tag()) {
            tags= mp3File.getID3v2Tag();
        }
        else tags = null;
    }

    @Override
    public String getName(){
        return tags.getSongTitle();
    }

    @Override
    public String getAlbum() {
        return tags.getAlbumTitle();
    }

    @Override
    public int getYear()   {
        return Integer.parseInt(tags.getYearReleased());
    }

    @Override
    public String getNotes()   {
        return tags.getSongComment();
    }
}