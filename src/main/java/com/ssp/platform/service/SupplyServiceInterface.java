package com.ssp.platform.service;

import com.ssp.platform.dto.SupplyDTO;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;

import java.util.*;

public interface SupplyServiceInterface {

    public void create(UUID purchaseId, Long cost, String technologyStack, String structure, String comment) throws SupplyException;

    public void update(UUID id, Long cost, String technologyStack, String structure, String comment) throws SupplyException;

    public void delete(UUID id) throws SupplyException;

    public SupplyDTO get(UUID id) throws SupplyException;

    List<SupplyDTO> getPage(int pageIndex, int pageSize) throws PageIndexException, PageSizeException;
}