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

    public void create(UUID purchaseId, String description, User authorId, Long budget, String comment, MultipartFile file)
            throws SupplyException, IOException, NoSuchAlgorithmException;

    public void update(UUID id, String description, Long budget, String comment, MultipartFile file)
            throws SupplyException, IOException, NoSuchAlgorithmException;

    public void update(UUID id, SupplyStatus status, String result) throws SupplyException;

    public void delete(UUID id) throws SupplyException, IOException;

    public SupplyResponse get(UUID id) throws SupplyException;

    List<SupplyResponse> getPage(UUID purchaseId, int pageIndex, int pageSize) throws PageIndexException, PageSizeException;
}