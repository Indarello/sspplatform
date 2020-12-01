package com.ssp.platform.service.impl;

import com.ssp.platform.property.FileProperty;
import com.ssp.platform.entity.FileEntity;
import com.ssp.platform.repository.FileRepository;
import com.ssp.platform.response.FileResponse;
import com.ssp.platform.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private final FileRepository fileRepository;

    private final Path fileStorageLocation;

    @Autowired
    public FileServiceImpl(FileProperty fileProperty, FileRepository fileRepository) throws IOException {
        fileStorageLocation = Paths.get(fileProperty.getUploadDirectory()).toAbsolutePath().normalize();
        this.fileRepository = fileRepository;
        if (!Files.exists(fileStorageLocation)) {
            Files.createDirectory(fileStorageLocation);
        }
    }

    @Override
    public FileEntity save(FileEntity file)
    {
        return fileRepository.save(file);
    }

    @Override
    public FileEntity addFile(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        return createFile(file);
    }

    @Override
    public FileResponse getFile(UUID id) throws MalformedURLException {
        Optional<FileEntity> searchResult = fileRepository.findById(id);

        if (searchResult.isPresent()) {
            FileEntity fileEntity = searchResult.get();
            String extension = fileEntity.getName().substring(fileEntity.getName().lastIndexOf("."));
            Path filePath = fileStorageLocation.resolve(fileEntity.getHash() + extension).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            return new FileResponse(true, resource, fileEntity);
        }
        return new FileResponse(false, null, null);
    }

    @Override
    public void delete(UUID id) throws IOException {
        FileEntity fileEntity = fileRepository.getOne(id);

        if (fileEntity.getSupplies().isEmpty()){
            String extension = fileEntity.getName().substring(fileEntity.getName().lastIndexOf("."));

            deleteFile(fileEntity.getHash(), extension);
            fileRepository.delete(fileEntity);
        }
    }

    private FileEntity createFile(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setMimeType(file.getContentType());
        fileEntity.setSize(file.getSize());
        fileEntity.setHash();

        if (fileRepository.existsByHash(fileEntity.getHash())){
            fileEntity = fileRepository.getOneByHash(fileEntity.getHash());
            return fileEntity;
        } else {
            String extension = fileEntity.getName().substring(fileEntity.getName().lastIndexOf("."));
            storeFile(file, fileEntity.getHash(), extension);

            return fileRepository.save(fileEntity);
        }
    }

    private void storeFile(MultipartFile file, String hash, String fileExtension) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(hash + fileExtension);
        Files.copy(file.getInputStream(), targetLocation);
    }

    private void deleteFile(String hash, String fileExtension) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(hash + fileExtension);
        Files.delete(targetLocation);
    }

}
