package com.epam.songmanager.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties(prefix = "md")
@Data
public class MessageDigestProperties {
    private  String messageDigestType;

}


