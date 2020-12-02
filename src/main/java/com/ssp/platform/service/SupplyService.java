package com.ssp.platform.service;

import com.ssp.platform.response.SupplyResponse;
import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public interface SupplyService {

    void create(UUID purchaseId, String description, User authorId, Long budget, String comment, MultipartFile file)
     throws SupplyException, IOException, NoSuchAlgorithmException;

    void update(UUID id, String description, Long budget, String comment, MultipartFile file)
     throws SupplyException, IOException, NoSuchAlgorithmException;

    void update(UUID id, SupplyStatus status, String result) throws SupplyException;

    void delete(UUID id) throws SupplyException, IOException;

    SupplyResponse get(UUID id) throws SupplyException;

    List<SupplyResponse> getPage(UUID purchaseId, Integer pageIndex, Integer pageSize) throws PageIndexException, PageSizeException;
}