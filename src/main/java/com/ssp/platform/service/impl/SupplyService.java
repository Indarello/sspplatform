package com.ssp.platform.service.impl;

import com.ssp.platform.dto.SupplyDTO;
import com.ssp.platform.entity.SupplyEntity;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.repository.SupplyRepository;
import com.ssp.platform.response.ValidatorResponse;
import com.ssp.platform.service.SupplyServiceInterface;
import com.ssp.platform.validate.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SupplyService implements SupplyServiceInterface {

    private final SupplyRepository supplyRepository;

    private final SupplyValidator supplyValidator;

    public SupplyService(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;

        supplyValidator = new SupplyValidator();
    }

    @Override
    public void create(UUID purchaseId, Long cost, String technologyStack, String structure, String comment) throws SupplyException {

        //supplyRepository.save(supplyEntity);
    }

    @Override
    public void update(UUID id, Long cost, String technologyStack, String structure, String comment) throws SupplyException {


        //supplyRepository.saveAndFlush(supplyEntity);
    }

    @Override
    public void delete(UUID id) throws SupplyException {


        supplyRepository.delete(supplyRepository.getOne(id));
    }

    @Override
    public SupplyDTO get(UUID id) throws SupplyException {

        SupplyEntity supplyEntity = supplyRepository.getOne(id);
        return null;
        //return new SupplyDTO(supplyEntity.getCost(), supplyEntity.getTStack(), supplyEntity.getStructure(), supplyEntity.getComment());
    }

    @Override
    public List<SupplyDTO> getPage(int pageIndex, int pageSize) throws PageIndexException, PageSizeException {
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
        Page<SupplyEntity> page = supplyRepository.findAll(pageable);

        List<SupplyDTO> out = new ArrayList<>();
        for (SupplyEntity supplyEntity : page){
            //out.add(new SupplyDTO(supplyEntity.getCost(), supplyEntity.getTStack(), supplyEntity.getStructure(), supplyEntity.getComment()));
        }

        return out;
    }
}
