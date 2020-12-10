package com.ssp.platform.controller;

import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.FileResponse;
import com.ssp.platform.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<?> getFile(@PathVariable("fileId") UUID id) throws IOException {
        if (id == null) {
            return new ResponseEntity<>(new ApiResponse(false, "Параметр id не предоставлен"), HttpStatus.NOT_ACCEPTABLE);
        }

        FileResponse fileResponse = fileService.getFile(id);
        if(!fileResponse.isSuccess()) {
            return new ResponseEntity<>(new ApiResponse(false, "Файл не найден"), HttpStatus.NOT_FOUND);
        } else {
            Resource resource = fileResponse.getResource();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(resource.getFilename())))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
    }

    @DeleteMapping("/file/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileId") UUID id) throws IOException {
        fileService.delete(id);

        return new ResponseEntity<>(new ApiResponse(true, "Файл удалён"), HttpStatus.OK);
    }
}
