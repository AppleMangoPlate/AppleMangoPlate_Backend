package com.Applemango_Backend.image.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class StorageConfig {

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource resource = new ClassPathResource("applemango-image-a28687b6b080.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setProjectId("applemango-image")
                .setCredentials(credentials).build();
        Storage storage = storageOptions.getService();
        return storage;
    }
}
