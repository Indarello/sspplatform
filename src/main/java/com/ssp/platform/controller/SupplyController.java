package com.ssp.platform.controller;

import com.ssp.platform.dto.SupplyDTO;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.service.impl.SupplyService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class SupplyController {

    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @PostMapping("/supply")
    public void createSupply(@RequestParam UUID purchaseId, @RequestParam Long cost, @RequestParam String tStack,
            @RequestParam String structure, @RequestParam String comment) throws SupplyException {
        supplyService.create(purchaseId, cost, tStack, structure, comment);
    }

    @PutMapping("/supply/{id}")
    public void updateSupply(@PathVariable("id") UUID id, @RequestParam Long cost, @RequestParam String tStack,
            @RequestParam String structure, @RequestParam String comment) throws SupplyException {
        supplyService.update(id, cost, tStack, structure, comment);
    }

    @DeleteMapping("/supply/{id}")
    public void deleteSupply(@PathVariable("id") UUID id) throws SupplyException {
        supplyService.delete(id);
    }

    @GetMapping("/supply/{id}")
    public SupplyDTO getSupply(@PathVariable("id") UUID id) throws SupplyException {
        return supplyService.get(id);
    }

    @GetMapping("/supply")
    public List<SupplyDTO> getPageOfSupplies(@RequestParam int pageIndex, @RequestParam int pageSize) throws PageSizeException, PageIndexException {
        return supplyService.getPage(pageIndex, pageSize);
    }
}
