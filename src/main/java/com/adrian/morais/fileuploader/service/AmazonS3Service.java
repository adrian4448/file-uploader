package com.adrian.morais.fileuploader.service;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service {

    void UploadFile(MultipartFile file);
    String getUrlDownload(String fileName);
}
