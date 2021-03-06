package com.epam.songmanager.service.impl;

import com.epam.songmanager.config.properties.LocationProperties;
import com.epam.songmanager.exceptions.StorageException;
import com.epam.songmanager.exceptions.StorageFileNotFoundException;
import com.epam.songmanager.model.entity.StorageType;
import com.epam.songmanager.model.resource.FileStorageEntity;
import com.epam.songmanager.model.resource.ResourceObj;
import com.epam.songmanager.service.interfaces.StorageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class FileSystemStorageService implements StorageService<FileStorageEntity> {

    private final  StorageType storageType = StorageType.DISK_FILE_SYSTEM;

    //private final Path rootLocation  = Paths.get("C:\\Users\\Admin\\Desktop\\Song");

    public String store(InputStream stream) throws IOException {
        if (stream.available()==0) {
            throw new StorageException("Failed to store empty file.");
        }
        else{
        Path path =createFilepath();
        createFiles(stream,path.toString());
        return  path.toString();
        }
    }

    private Path createFilepath(){
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        Path destinationFile = Paths.get(uuidAsString+".mp3");
       // return rootLocation.resolve(destinationFile);
        return  null;
    }

    private void createFiles(InputStream stream, String path) throws IOException {
        File file = new File(path);
        try ( stream; OutputStream os = new FileOutputStream(file)) {
            IOUtils.copy(stream, os);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<String> loadAll() {

//         List<Path> paths = Files.walk(rootLocation, 1)
//                    .filter(p -> !p.equals(rootLocation))
//                    .map(rootLocation::relativize).collect(Collectors.toList());
         List<String> stringPaths = new LinkedList<>();

//         paths.forEach(p->stringPaths.add(p.toString()));

             return  stringPaths;

//        catch (IOException e) {
//            throw new StorageException("Failed to read stored files", e);
//        }
    }

    @Override
    public Path load(String filename) {
        //return rootLocation.resolve(filename);
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {

        //FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public FileStorageEntity create(String cs, String path, long size) {
//        return new FileStorageEntity(path, size,cs);
        return null;
    }

    @Override
    public Resource getResource(String filename) throws IOException {
        return new ByteArrayResource( new FileInputStream(filename).readAllBytes());
    }


    @Override
    public boolean supports(StorageType resource) {
        return resource.equals(storageType);
    }

    @Override
    public boolean supports(Class<? extends ResourceObj> clazz) {
        if(clazz.equals(FileStorageEntity.class))
            return true ;
        return false;
    }

    @Override
    public Class<? extends ResourceObj> getSupportsClass() {
        return FileStorageEntity.class;
    }
}