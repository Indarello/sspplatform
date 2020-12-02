package com.ssp.platform.controller;

import com.ssp.platform.response.SupplyResponse;
import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.impl.*;
import org.slf4j.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
public class SupplyController {

    private final SupplyServiceImpl supplyService;

    private final UserDetailsServiceImpl userDetailsService;

    Logger logger = LoggerFactory.getLogger(SupplyController.class);

    public SupplyController(SupplyServiceImpl supplyService, UserDetailsServiceImpl userDetailsService) {
        this.supplyService = supplyService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/supply")
    @PreAuthorize("hasAuthority('firm')")
    public void createSupply(
            @RequestHeader("Authorization") String token, @RequestParam UUID purchaseId, @RequestParam String description, @RequestParam Long budget,
            @RequestParam String comment, @RequestParam(value = "file") MultipartFile file) throws SupplyException, IOException, NoSuchAlgorithmException {

        User author = userDetailsService.loadUserByToken(token);
        supplyService.create(purchaseId, description, author, budget, comment, file);
    }

    @PutMapping("/supply/{id}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public void updateSupply(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long budget,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false) SupplyStatus status,
            @RequestParam(required = false) String result,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws SupplyException, IOException, NoSuchAlgorithmException {

        switch (userDetailsService.loadUserByToken(token).getRole()){
            case "firm":
                if (status == null && result == null) supplyService.update(id, description, budget, comment, file);
                break;

            case "employee":
                if (description == null && budget == null && comment == null && file == null) supplyService.update(id, status, result);
                break;
        }
    }

    @DeleteMapping("/supply/{id}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public void deleteSupply(@RequestHeader("Authorization") String token, @PathVariable("id") UUID id) throws SupplyException, IOException {
        User user = userDetailsService.loadUserByToken(token);
        switch (user.getRole()) {
            case "firm":
                SupplyResponse supplyResponse = supplyService.get(id);
                if (user.getUsername().equals(supplyResponse.getAuthor())){
                    supplyService.delete(id);
                }
                break;

            case "employee":
                supplyService.delete(id);
                break;
        }
    }

    @GetMapping("/supply/{id}/firm")
    @PreAuthorize("hasAuthority('firm')")
    public SupplyResponse getSupply(@RequestHeader("Authorization") String token, @PathVariable("id") UUID id) throws SupplyException {
        User user = userDetailsService.loadUserByToken(token);
        switch (user.getRole()){
            case "firm":
                SupplyResponse supplyResponse = supplyService.get(id);
                if (user.getUsername().equals(supplyResponse.getAuthor())){
                    return supplyResponse;
                }
                break;

            case "employee":
                return supplyService.get(id);
        }
        return null;
    }

    @GetMapping("/supplies")
    @PreAuthorize("hasAuthority('employee')")
    public List<SupplyResponse> getPageOfSupplies(
            @RequestParam UUID purchaseId,
            @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) throws PageSizeException, PageIndexException {
        return supplyService.getPage(purchaseId, pageIndex, pageSize);
    }
}
