package com.ssp.platform.service;

import com.ssp.platform.entity.*;
import com.ssp.platform.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public interface FileService {

    FileEntity save(FileEntity file);

    FileEntity addFile(MultipartFile file, UUID id, int location) throws NoSuchAlgorithmException, IOException;

    FileResponse getFile(UUID id) throws MalformedURLException;

    void delete(UUID id) throws IOException;

}
