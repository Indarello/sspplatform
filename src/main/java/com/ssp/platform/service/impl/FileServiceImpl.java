package com.ssp.platform.service.impl;

import com.ssp.platform.entity.*;
import com.ssp.platform.exceptions.FileValidationException;
import com.ssp.platform.property.FileProperty;
import com.ssp.platform.repository.FileRepository;
import com.ssp.platform.response.FileResponse;
import com.ssp.platform.service.FileService;
import com.ssp.platform.validate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {
    public static final int LOCATION_PURCHASE = 0;
    public static final int LOCATION_SUPPLY = 1;

    private final FileRepository fileRepository;

    private final FileValidatorNew fileValidator;

    private final Path fileStorageLocation;

    @Autowired
    public FileServiceImpl(FileProperty fileProperty, FileRepository fileRepository, FileValidatorNew fileValidator) throws IOException {
        fileStorageLocation = Paths.get(fileProperty.getUploadDirectory()).toAbsolutePath().normalize();
        this.fileRepository = fileRepository;
        if (!Files.exists(fileStorageLocation)) {
            Files.createDirectory(fileStorageLocation);
        }
        this.fileValidator = fileValidator;
    }

    @Override
    public FileEntity save(FileEntity file) {
        return fileRepository.save(file);
    }

    @Override
    public FileEntity addFile(MultipartFile file, UUID id, int location) throws NoSuchAlgorithmException, IOException {
        return createFile(file, id, location);
    }

    @Override
    public List<FileEntity> addFiles(MultipartFile[] files, UUID id, int location) throws NoSuchAlgorithmException, IOException, FileValidationException {
        fileValidator.validateFiles(files);

        List<FileEntity> fileEntities = new ArrayList<>();

        for (MultipartFile file : files){
            FileEntity fileEntity = new FileEntity(
                    file.getOriginalFilename(),
                    file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")),
                    file.getSize()
            );

            if (location == LOCATION_PURCHASE) fileEntity.setPurchase(id);
            if (location == LOCATION_SUPPLY) fileEntity.setSupply(id);

            fileEntities.add(fileEntity);
        }

        storeFiles(fileEntities, files);
        return fileRepository.saveAll(fileEntities);
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

        String extension = fileEntity.getName().substring(fileEntity.getName().lastIndexOf("."));
        deleteFile(fileEntity.getHash(), extension);

        fileRepository.delete(fileEntity);
    }

    private FileEntity createFile(MultipartFile file, UUID id, int location) throws NoSuchAlgorithmException, IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setType(file.getContentType());
        fileEntity.setSize(file.getSize());
        fileEntity.setHash();

        if (location == LOCATION_PURCHASE) fileEntity.setPurchase(id);
        if (location == LOCATION_SUPPLY) fileEntity.setSupply(id);

        String extension = fileEntity.getName().substring(fileEntity.getName().lastIndexOf("."));
        storeFile(file, fileEntity.getHash(), extension);

        return fileRepository.save(fileEntity);
    }

    private void storeFile(MultipartFile file, String hash, String fileExtension) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(hash + fileExtension);
        Files.copy(file.getInputStream(), targetLocation);
    }

    private void storeFiles(List<FileEntity> fileEntities, MultipartFile[] files) throws IOException {
        for (int i = 0; i < files.length; ++i) {
            Path targetLocation = fileStorageLocation.resolve(fileEntities.get(i).getHash() + fileEntities.get(i).getType());
            Files.copy(files[i].getInputStream(), targetLocation);
        }
    }

    private void deleteFile(String hash, String fileExtension) throws IOException {
        Path targetLocation = fileStorageLocation.resolve(hash + fileExtension);
        Files.delete(targetLocation);
    }

}
