package com.ssp.platform.service;

import com.ssp.platform.exceptions.*;
import com.ssp.platform.request.SupplyUpdateRequest;
import com.ssp.platform.response.SupplyResponse;
import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.exceptions.PageExceptions.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public interface SupplyService {

    void create(UUID purchaseId, String description, User authorId, Long budget, String comment, MultipartFile[] files)
            throws SupplyException, IOException, NoSuchAlgorithmException, SupplyValidationException, FileValidationException, SupplyServiceException;

    void update(User user, UUID id, SupplyUpdateRequest updateRequest)
            throws SupplyException, IOException, NoSuchAlgorithmException, SupplyValidationException, SupplyServiceException, FileValidationException;

    void delete(User user, UUID id) throws SupplyException, IOException, FileServiceException, SupplyServiceException;

    SupplyEntity get(User user, UUID id) throws SupplyException;

    List<SupplyEntity> getList(UUID purchaseId);
}