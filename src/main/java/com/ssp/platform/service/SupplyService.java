package com.ssp.platform.service;

import com.ssp.platform.request.SupplyUpdateRequest;
import com.ssp.platform.response.SupplyResponse;
import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public interface SupplyService {

    void create(UUID purchaseId, String description, User authorId, Long budget, String comment, MultipartFile file)
     throws SupplyException, IOException, NoSuchAlgorithmException;

    void update(User user, UUID id, SupplyUpdateRequest updateRequest) throws SupplyException;

    void delete(User user, UUID id) throws SupplyException, IOException;

    SupplyEntity get(User user, UUID id) throws SupplyException;

    List<SupplyEntity> getPage(UUID purchaseId, Integer pageIndex, Integer pageSize) throws PageIndexException, PageSizeException;
}