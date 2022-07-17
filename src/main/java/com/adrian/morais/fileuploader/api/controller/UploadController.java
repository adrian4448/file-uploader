package com.adrian.morais.fileuploader.api.controller;

import com.adrian.morais.fileuploader.api.controller.dto.UploadedFileDTO;
import com.adrian.morais.fileuploader.service.AmazonS3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;

@RestController
public class UploadController {

    private AmazonS3Service amazonS3Service;

    public UploadController(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @PostMapping("upload")
    public ResponseEntity uploadFile(@PathParam("file") MultipartFile file) {
        amazonS3Service.UploadFile(file);
        return ResponseEntity.ok().body("The File: " + file.getOriginalFilename() + "is Uploaded");
    }

    @GetMapping("signed-url")
    public ResponseEntity downloadFile(@PathParam("fileName") String fileName) {
        return ResponseEntity.ok().body(new UploadedFileDTO(amazonS3Service.getUrlDownload(fileName)));
    }

}
