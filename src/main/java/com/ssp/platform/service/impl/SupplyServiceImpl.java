package com.ssp.platform.service.impl;

import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.repository.*;
import com.ssp.platform.request.SupplyUpdateRequest;
import com.ssp.platform.response.*;
import com.ssp.platform.service.*;
import com.ssp.platform.validate.*;
import com.ssp.platform.validate.ValidatorMessages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.ssp.platform.validate.ValidatorMessages.SupplyValidatorMessages.WRONG_ROLE_FOR_UPDATING;

@Service
public class SupplyServiceImpl implements SupplyService {

    public static final long DATE_DIVIDER = 1000L;

    public static final int MAX_FILES = 20;

    private final SupplyRepository supplyRepository;

    private final PurchaseRepository purchaseRepository;

    private final FileServiceImpl fileService;

    private final SupplyValidator supplyValidator;

    private final FileValidator fileValidator;

    public SupplyServiceImpl(
            SupplyRepository supplyRepository, FileServiceImpl fileService, SupplyValidator supplyValidator, FileValidator fileValidator,
            PurchaseRepository purchaseRepository) {
        this.supplyRepository = supplyRepository;
        this.fileService = fileService;
        this.supplyValidator = supplyValidator;
        this.fileValidator = fileValidator;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public void create(UUID purchaseId, String description, User author, Long budget, String comment, MultipartFile[] files)
            throws SupplyException, IOException, NoSuchAlgorithmException {

        if (supplyRepository.existsByAuthorAndPurchaseId(author, purchaseId)) {
            throw new SupplyException(new ValidateResponse(false, SupplyValidatorMessages.SUPPLY_ALREADY_EXIST_BY_USER_ERROR));
        }

        if (files != null && files.length > 20){
            throw new SupplyException(new ValidateResponse(false, "files", FileValidatorMessages.TOO_MUCH_FILES));
        }

        SupplyEntity supplyEntity = new SupplyEntity(purchaseRepository.getOne(purchaseId), description, author, budget, comment);
        ValidateResponse response = supplyValidator.validateSupplyCreating(supplyEntity);
        if (!response.isSuccess()) throw new SupplyException(response);

        if (files != null && files.length > 0){
            for (MultipartFile file : files){
                response = fileValidator.validateFile(file);
                if (!response.isSuccess()) throw new SupplyException(response);

                fileService.addFile(file, supplyEntity.getId(), FileServiceImpl.LOCATION_SUPPLY);
            }
        }

        supplyRepository.save(supplyEntity);
    }

    @Override
    public void update(User user, UUID id, SupplyUpdateRequest updateRequest) throws SupplyException, IOException, NoSuchAlgorithmException {
        ValidateResponse validatorResponse;
        SupplyEntity supplyEntity = supplyRepository.getOne(id);

        switch (user.getRole()){
            case "firm":
                if (user.equals(supplyEntity.getAuthor())){
                    validatorResponse = supplyValidator.validateSupplyUpdating(updateRequest, supplyEntity, SupplyValidator.ROLE_FIRM);
                    if (!validatorResponse.isSuccess()) throw new SupplyException(validatorResponse);
                }

                if (updateRequest.getFiles() != null && updateRequest.getFiles().length > 0){
                    if (supplyEntity.getFiles().size() + updateRequest.getFiles().length > MAX_FILES){
                        throw new SupplyException(new ValidateResponse(false, "files", FileValidatorMessages.TOO_MUCH_FILES));
                    }
                    for (MultipartFile file : updateRequest.getFiles()){
                        ValidateResponse response = fileValidator.validateFile(file);
                        if (!response.isSuccess()) throw new SupplyException(response);

                        fileService.addFile(file, supplyEntity.getId(), FileServiceImpl.LOCATION_SUPPLY);
                    }
                }

                if (updateRequest.getDescription() != null && !updateRequest.getDescription().isEmpty()){
                    supplyEntity.setDescription(updateRequest.getDescription());
                }

                if (updateRequest.getBudget() != null){
                    supplyEntity.setBudget(updateRequest.getBudget());
                }

                if (updateRequest.getComment() != null && !updateRequest.getComment().isEmpty()){
                    supplyEntity.setComment(updateRequest.getComment());
                }

                if (updateRequest.getFiles() != null && updateRequest.getFiles().length > MAX_FILES){
                    throw new SupplyException(new ValidateResponse(false, "files", FileValidatorMessages.TOO_MUCH_FILES));
                }

                break;

            case "employee":
                if (updateRequest.getFiles() != null && updateRequest.getFiles().length > 0){
                    throw new SupplyException(new ValidateResponse(false, "files", WRONG_ROLE_FOR_UPDATING));
                }

                validatorResponse = supplyValidator.validateSupplyUpdating(updateRequest, supplyEntity, SupplyValidator.ROLE_EMPLOYEE);
                if (!validatorResponse.isSuccess()) throw new SupplyException(validatorResponse);

                if (updateRequest.getStatus() != null){
                    supplyEntity.setStatus(updateRequest.getStatus());
                }

                if (updateRequest.getResult() != null && !updateRequest.getResult().isEmpty()){
                    supplyEntity.setResult(updateRequest.getResult());
                }

                supplyEntity.setResultDate(System.currentTimeMillis() / DATE_DIVIDER);

                break;
        }

        supplyRepository.saveAndFlush(supplyEntity);
    }

    @Override
    public void delete(User user, UUID id) throws SupplyException, IOException {
        SupplyEntity supplyEntity = supplyRepository.getOne(id);

        switch (user.getRole()) {
            case "firm":
                if (user.equals(supplyEntity.getAuthor())){
                    supplyRepository.delete(supplyEntity);
                    List<FileEntity> files = supplyEntity.getFiles();
                    for (FileEntity file : files){
                        fileService.delete(file.getId());
                    }
                }
                break;

            case "employee":
                supplyRepository.delete(supplyEntity);
                List<FileEntity> files = supplyEntity.getFiles();
                for (FileEntity file : files){
                    fileService.delete(file.getId());
                }
                break;
        }
    }

    @Override
    public SupplyEntity get(User user, UUID id) throws SupplyException {
        SupplyEntity supplyEntity = supplyRepository.getOne(id);

        switch (user.getRole()){
            case "firm":
                if (user.equals(supplyEntity.getAuthor())){
                    return supplyEntity;
                }
                break;

            case "employee":
                return supplyEntity;
        }

        return null;
    }

    @Override
    public List<SupplyEntity> getPage(UUID purchaseId, Integer pageIndex, Integer pageSize) throws PageIndexException, PageSizeException {
        if (pageIndex == null) pageIndex = 0;
        if (pageSize == null ) pageSize = 10;

        if (pageIndex < 0) {
            throw new PageIndexException();
        }
        if (pageSize < 1) {
            throw new PageSizeException();
        }
        if (pageSize > 100) {
            pageSize = 10;
        }

        List<SupplyEntity> list = supplyRepository.findAllByPurchase(purchaseRepository.getOne(purchaseId));
        list.sort((o1, o2) -> {
            if (o2.getStatus() == o1.getStatus()) {
                return o2.getCreateDate().compareTo(o1.getCreateDate());
            }
            return o1.getStatus().compareTo(o2.getStatus());
        });
        return list;
    }
}
