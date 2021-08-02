package com.epam.songmanager.utils.impl;

import com.epam.songmanager.exceptions.StorageException;
import com.epam.songmanager.model.file_entity.BaseFile;
import com.epam.songmanager.model.file_entity.FileStorageEntity;
import com.epam.songmanager.utils.Converter;
import com.epam.songmanager.utils.InpStreamClone;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;


@Component
public class ConverterToTempMp3File<T extends BaseFile>  implements Converter<T> {

    File tmpFile = File.createTempFile("data", ".mp3");
    OutputStream outputStream = new FileOutputStream(tmpFile);


    public ConverterToTempMp3File() throws IOException {
    }

    @Override
    public File converting(T entity) throws IOException {

        File tmpFile = File.createTempFile("data", ".mp3");

        try(OutputStream outputStream = new FileOutputStream(tmpFile)){
            IOUtils.copy(entity.getInputStreamClone(), outputStream);
            return tmpFile;
        } catch (FileNotFoundException e) {
            throw new StorageException("Failed to find files", e);
        } catch (IOException e) {
            throw new StorageException("Failed to stored files", e);
        }

    }

    @Override
    public boolean delete(T entity) throws IOException {
        File tmpFile = new File(entity.getPath());
       return tmpFile.delete();
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }
}
