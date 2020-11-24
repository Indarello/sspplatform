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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileController
{

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService)
    {
        this.fileService = fileService;
    }

    /*
    @PostMapping("/file/upload")
    public ResponseEntity<Object> addFile(@RequestParam(value = "files") MultipartFile[] files) throws IOException, NoSuchAlgorithmException
    {
        System.out.println("123");

        if (files != null)
        {
            ValidateResponse validateResult = FileValidate.validateFiles(files);
            if (!validateResult.isSuccess())
            {
                if(validateResult.getField().equals("size")) files = null;

                else return new ResponseEntity<>(validateResult, HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                fileService.addFiles(files);
            }
        }


        return new ResponseEntity<>(new ApiResponse(true, "Успешно сохранили файл"), HttpStatus.CREATED);
    }
*/

    @GetMapping("/file/{fileId}")
    public ResponseEntity<?> getFile(@PathVariable("fileId") UUID id) throws IOException
    {
        if (id == null)
        {
            return new ResponseEntity<>(new ApiResponse(false, "Параметр id не предоставлен"), HttpStatus.NOT_ACCEPTABLE);
        }

        FileResponse fileResponse = fileService.getFile(id);
        if(!fileResponse.isSuccess())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Файл не найден"), HttpStatus.NOT_FOUND);
        }
        else
        {
            Resource resource = fileResponse.getResource();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileResponse.getFile().getMimeType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
    }
}
