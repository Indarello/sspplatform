package com.ssp.platform.service.impl;

import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.repository.*;
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

@Service
public class SupplyServiceImpl implements SupplyService {

    public static final long DATE_DIVIDER = 1000L;

    private final SupplyRepository supplyRepository;

    private final PurchaseRepository purchaseRepository;

    private final FileServiceImpl fileService;

    private final SupplyValidator supplyValidator;

    private final FileValidator fileValidator;

    public SupplyServiceImpl(
            SupplyRepository supplyRepository, FileServiceImpl fileService, SupplyValidator supplyValidator, FileValidator fileValidator,
            PurchaseRepository purchaseRepository
    ) {
        this.supplyRepository = supplyRepository;
        this.fileService = fileService;
        this.supplyValidator = supplyValidator;
        this.fileValidator = fileValidator;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public void create(UUID purchaseId, String description, User author, Long budget, String comment, MultipartFile file)
            throws SupplyException, IOException, NoSuchAlgorithmException {

        if (supplyRepository.existsByAuthorAndPurchaseId(author, purchaseId)) {
            throw new SupplyException(new ValidatorResponse(false, SupplyValidatorMessages.SUPPLY_ALREADY_EXIST_BY_USER_ERROR));
        }

        ValidatorResponse response = fileValidator.validateFile(file);
        if (!response.isSuccess()) throw new SupplyException(response);

        FileEntity fileEntity = null;
        if (file == null){
            fileEntity = fileService.addFile(file);
        }
        SupplyEntity supplyEntity = new SupplyEntity(purchaseRepository.getOne(purchaseId), description, author, budget, comment, fileEntity);

        response = supplyValidator.validateSupplyCreating(supplyEntity);
        if (!response.isSuccess()) throw new SupplyException(response);

        supplyRepository.save(supplyEntity);
    }

    @Override
    public void update(UUID id, String description, Long budget, String comment, MultipartFile file)
            throws SupplyException, IOException, NoSuchAlgorithmException {
        FileEntity fileEntity = null;
        ValidatorResponse response = null;

        if (file != null){
            response = fileValidator.validateFile(file);
            if (!response.isSuccess()) throw new SupplyException(response);
            fileEntity = fileService.addFile(file);
        }

        SupplyEntity supplyEntity = supplyRepository.getOne(id);

        if (description != null && !description.isEmpty()) supplyEntity.setDescription(description);
        if (budget != null) supplyEntity.setBudget(budget);
        if (comment != null && !comment.isEmpty()) supplyEntity.setComment(comment);
        if (fileEntity != null) supplyEntity.setFile(fileEntity);

        response = supplyValidator.validateSupplyUpdating(supplyEntity, SupplyValidator.ROLE_FIRM);
        if (!response.isSuccess()) throw new SupplyException(response);

        supplyRepository.saveAndFlush(supplyEntity);
    }

    @Override
    public void update(UUID id, SupplyStatus status, String result) throws SupplyException {
        SupplyEntity supplyEntity = supplyRepository.getOne(id);

        supplyEntity.setStatus(status);
        supplyEntity.setResultOfConsideration(result);
        supplyEntity.setResultDate(System.currentTimeMillis() / DATE_DIVIDER);

        ValidatorResponse response = supplyValidator.validateSupplyUpdating(supplyEntity, SupplyValidator.ROLE_EMPLOYEE);
        if (!response.isSuccess()) throw new SupplyException(response);

        supplyRepository.saveAndFlush(supplyEntity);
    }

    @Override
    public void delete(UUID id) throws SupplyException, IOException {
        SupplyEntity supplyEntity = supplyRepository.getOne(id);
        supplyRepository.delete(supplyEntity);

        fileService.delete(supplyEntity.getFile().getId());
    }

    @Override
    public SupplyResponse get(UUID id) throws SupplyException {
        SupplyEntity supplyEntity = supplyRepository.getOne(id);
        SupplyResponse supplyResponse = new SupplyResponse();

        supplyResponse.setDescription(supplyEntity.getDescription());
        supplyResponse.setCreateDate(supplyEntity.getCreateDate());
        supplyResponse.setAuthor(supplyEntity.getAuthor().getLastName() + " " + supplyEntity.getAuthor().getFirstName());
        supplyResponse.setBudget(supplyEntity.getBudget());
        supplyResponse.setComment(supplyEntity.getComment());
        supplyResponse.setStatus(supplyEntity.getStatus());
        supplyResponse.setResultOfConsideration(supplyEntity.getResultOfConsideration());
        supplyResponse.setResultDate(supplyEntity.getResultDate());

        FileDTO fileDTO = new FileDTO(
                supplyEntity.getFile().getName(),
                supplyEntity.getFile().getMimeType(),
                supplyEntity.getFile().getSize(),
                supplyEntity.getFile().getHash()
        );
        supplyResponse.setFile(fileDTO);

        return supplyResponse;
    }

    @Override
    public List<SupplyResponse> getPage(UUID purchaseId, Integer pageIndex, Integer pageSize) throws PageIndexException, PageSizeException {
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

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<SupplyEntity> page = supplyRepository.findAllByPurchase(purchaseRepository.getOne(purchaseId), pageable);

        List<SupplyResponse> out = new ArrayList<>();
        for (SupplyEntity supplyEntity : page){
            SupplyResponse supplyResponse = new SupplyResponse();

            supplyResponse.setDescription(supplyEntity.getDescription());
            supplyResponse.setCreateDate(supplyEntity.getCreateDate());
            supplyResponse.setAuthor(supplyEntity.getAuthor().getUsername());
            supplyResponse.setBudget(supplyEntity.getBudget());
            supplyResponse.setComment(supplyEntity.getComment());
            supplyResponse.setStatus(supplyEntity.getStatus());
            supplyResponse.setResultOfConsideration(supplyEntity.getResultOfConsideration());
            supplyResponse.setResultDate(supplyEntity.getResultDate());

            if (supplyEntity.getFile() != null){
                FileDTO fileDTO = new FileDTO(
                        supplyEntity.getFile().getName(),
                        supplyEntity.getFile().getMimeType(),
                        supplyEntity.getFile().getSize(),
                        supplyEntity.getFile().getHash()
                );
                supplyResponse.setFile(fileDTO);
            }

            out.add(supplyResponse);
        }

        return out;
    }
}
