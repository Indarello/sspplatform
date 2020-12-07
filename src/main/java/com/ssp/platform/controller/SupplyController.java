package com.ssp.platform.controller;

import com.ssp.platform.request.SupplyUpdateRequest;
import com.ssp.platform.response.*;
import com.ssp.platform.entity.*;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.exceptions.PageExceptions.*;
import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.impl.*;
import org.slf4j.*;
import org.springframework.http.*;
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
    public ResponseEntity<Object> createSupply(
            @RequestHeader("Authorization") String token,
            @RequestParam UUID purchaseId,
            @RequestParam String description,
            @RequestParam(required = false) Long budget,
            @RequestParam(required = false) String comment,
            @RequestParam(value = "files", required = false) MultipartFile[] files) throws SupplyException, IOException, NoSuchAlgorithmException {

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
            @RequestParam(required = false) MultipartFile[] files) throws SupplyException, IOException, NoSuchAlgorithmException {

        logger.info("received: " + description + "|" + budget + "|" + comment + "|" + status + "|" + result);

        SupplyUpdateRequest updateRequest = new SupplyUpdateRequest(description, budget, comment, status, result, files);

        User user = userDetailsService.loadUserByToken(token);
        supplyService.update(user, id, updateRequest);

        return new ResponseEntity<>(supplyService.get(user, id), HttpStatus.OK);
    }

    @DeleteMapping("/supply/{id}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> deleteSupply(@RequestHeader("Authorization") String token, @PathVariable("id") UUID id) throws SupplyException, IOException {
        User user = userDetailsService.loadUserByToken(token);
        supplyService.delete(user, id);

        return new ResponseEntity<>(new ApiResponse(true, "Предложение удалено"), HttpStatus.OK);
    }

    @GetMapping("/supply/{id}")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getSupply(@RequestHeader("Authorization") String token, @PathVariable("id") UUID id) throws SupplyException {
        User user = userDetailsService.loadUserByToken(token);

        return new ResponseEntity<>(supplyService.get(user, id), HttpStatus.OK);
    }

    @GetMapping("/supplies")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getPageOfSupplies(@RequestParam UUID purchaseId, @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) throws PageSizeException, PageIndexException {
        return new ResponseEntity<>(supplyService.getPage(purchaseId, pageIndex, pageSize), HttpStatus.OK);
    }
}
