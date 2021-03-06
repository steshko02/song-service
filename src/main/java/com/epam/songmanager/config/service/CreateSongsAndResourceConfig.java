package com.epam.songmanager.config.service;

import com.epam.songmanager.config.properties.MessageDigestProperties;
import com.epam.songmanager.model.resource.CloudStorageEntity;
import com.epam.songmanager.model.resource.FileStorageEntity;
import com.epam.songmanager.service.impl.FileSystemStorageService;
import com.epam.songmanager.service.impl.MinioService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ MessageDigestProperties.class})
public class CreateSongsAndResourceConfig {

   

}
