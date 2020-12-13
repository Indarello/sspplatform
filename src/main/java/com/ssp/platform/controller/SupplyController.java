package com.ssp.platform.controller;

import com.ssp.platform.exceptions.*;
import com.ssp.platform.request.SupplyUpdateRequest;
import com.ssp.platform.response.*;
import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.SupplyService;
import com.ssp.platform.service.impl.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
public class SupplyController {

    private final SupplyService supplyService;
    private final UserDetailsServiceImpl userDetailsService;

    Logger logger = LoggerFactory.getLogger(SupplyController.class);

    @Autowired
    public SupplyController(SupplyService supplyService, UserDetailsServiceImpl userDetailsService) {
        this.supplyService = supplyService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/supply")
    @PreAuthorize("hasAuthority('firm')")
    public ResponseEntity<Object> createSupply(
            @RequestHeader("Authorization") String token,
            @RequestParam UUID purchaseId,
            @RequestParam String description,
            @RequestParam(required = false) Long budget,
            @RequestParam(required = false) String comment,
            @RequestParam(value = "files", required = false) MultipartFile[] files)
            throws IOException, NoSuchAlgorithmException, SupplyValidationException, FileValidationException, SupplyServiceException {

        User author = userDetailsService.loadUserByToken(token);
        supplyService.create(purchaseId, description, author, budget, comment, files);

        return new ResponseEntity<>(new ApiResponse(true, "Предложение создано"), HttpStatus.OK);
    }

    @PutMapping(value = "/supply/{id}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> updateSupply(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") UUID id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long budget,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false) SupplyStatus status,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) MultipartFile[] files)
            throws IOException, NoSuchAlgorithmException, SupplyValidationException, FileValidationException, SupplyServiceException {

        SupplyUpdateRequest updateRequest = new SupplyUpdateRequest(description, budget, comment, status, result, files);

        User user = userDetailsService.loadUserByToken(token);
        supplyService.update(user, id, updateRequest);

        return new ResponseEntity<>(supplyService.get(user, id), HttpStatus.OK);
    }

    @DeleteMapping("/supply/{id}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> deleteSupply(@RequestHeader("Authorization") String token, @PathVariable("id") UUID id)
            throws IOException, FileServiceException, SupplyServiceException {
        User user = userDetailsService.loadUserByToken(token);
        supplyService.delete(user, id);



        return new ResponseEntity<>(new ApiResponse(true, "Предложение удалено"), HttpStatus.OK);
    }

    @GetMapping("/supply/{id}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getSupply(@RequestHeader("Authorization") String token, @PathVariable("id") UUID id) {
        User user = userDetailsService.loadUserByToken(token);

        return new ResponseEntity<>(supplyService.get(user, id), HttpStatus.OK);
    }

    @GetMapping("/supplies")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getPageOfSupplies(@RequestParam UUID purchaseId) {
        return new ResponseEntity<>(supplyService.getList(purchaseId), HttpStatus.OK);
    }
}
