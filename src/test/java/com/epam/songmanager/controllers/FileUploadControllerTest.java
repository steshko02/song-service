package com.epam.songmanager.controllers;

import com.epam.songmanager.facades.CreateResource;
import com.epam.songmanager.model.entity.StorageType;
import com.epam.songmanager.model.resource.FileStorageEntity;
import com.epam.songmanager.repository.ArtistRepository;
import com.epam.songmanager.repository.GenreRepository;
import com.epam.songmanager.service.impl.FileSystemStorageService;
import com.epam.songmanager.service.interfaces.CreateFileSwitcher;
import com.epam.songmanager.service.interfaces.StorageService;
import com.epam.songmanager.service.interfaces.StorageSwitcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageSwitcher serviceStorageSwitcher;

    @MockBean
    private CreateFileSwitcher createFilesSwitcher;

    @MockBean
    private ArtistRepository artistRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private FileSystemStorageService fileSystemStorageService;

    private File fileObj = null;

    @After
    public  void  delete(){
        fileObj.delete();
    }

    @Test
    public void uploadsFile() throws Exception {

        String fileName = "sample-file-mock.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "uploaded-file",
                fileName,
                "text/plain",
                "This is the file content".getBytes());
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/");

        mockMvc.perform(multipartRequest.file(sampleFile));

    }

    @Test
    public void listUploadedFilesWhenDiskFS() throws Exception {

        StorageType storageType =StorageType.DISK_FILE_SYSTEM;

        List<String> paths = new ArrayList<>();
        paths.add("test1");
        paths.add("test2");
        paths.add("test3");

        doReturn(fileSystemStorageService).when(serviceStorageSwitcher).getByType(storageType);
        when( fileSystemStorageService.loadAll()).thenReturn(paths);

        mockMvc.perform(get("/")
                .param("st", String.valueOf(storageType)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("files", equalTo(paths)))
                .andExpect(model().attribute("storage", equalTo(storageType)))
        .andExpect(view().name("uploadForm"));
    }

    @Test
    void serveFile() throws Exception {
        StorageType storageType =StorageType.DISK_FILE_SYSTEM;
        String filename = "filename.mp3";
         fileObj = new File(filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileObj);
            fileOutputStream.write("test123".getBytes(StandardCharsets.UTF_8));
            doReturn(fileSystemStorageService).when(serviceStorageSwitcher).getByType(storageType);
            when( fileSystemStorageService.load(filename)).thenReturn(Path.of(filename));
            Path file = serviceStorageSwitcher.getByType(storageType).load(filename);
            Resource resource = new UrlResource(file.toUri());
            when( fileSystemStorageService.loadAsResource(filename)).thenReturn(resource);
            mockMvc.perform(
                    get("/filename.mp3").
                            param("st", String.valueOf(storageType)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("test123"));
        }
        catch (Exception e){
            throw new  Exception ("test Exception" + e);
        }
        finally {
            fileObj.delete();
        }

    }

}