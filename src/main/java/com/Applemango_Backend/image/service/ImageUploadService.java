package com.Applemango_Backend.image.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageUploadService {

    private final Storage storage;
    private String baseurl = "https://storage.googleapis.com/applemango-image/";

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString(); // 파일명 중복 방지
        String ext = file.getContentType();
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fileName)
                        .setContentType(ext)
                        .build(),
                file.getInputStream()
        );

        return baseurl+fileName;
    }
}
