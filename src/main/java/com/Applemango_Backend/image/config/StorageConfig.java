package com.Applemango_Backend.image.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class StorageConfig {

    @Bean
    public Storage storage() throws IOException {
        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setProjectId("applemango-image")
                .setCredentials(GoogleCredentials.fromStream(new
                        FileInputStream("C:/Users/조종현/Desktop/GDSC/Applemango_Backend/src/main/resources/applemango-image-a28687b6b080.json"))).build();
        Storage storage = storageOptions.getService();
        return storage;
    }
}
