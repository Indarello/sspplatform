package com.ssp.platform.controller;

import com.ssp.platform.entity.FileEntity;
import com.ssp.platform.entity.Purchase;
import com.ssp.platform.entity.User;
import com.ssp.platform.entity.enums.PurchaseStatus;
import com.ssp.platform.exceptions.FileValidationException;
import com.ssp.platform.request.PurchasesPageRequest;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.ValidateResponse;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.FileService;
import com.ssp.platform.service.PurchaseService;
import com.ssp.platform.service.impl.FileServiceImpl;
import com.ssp.platform.validate.PurchaseValidate;
import com.ssp.platform.validate.PurchasesPageValidate;
import com.ssp.platform.validate.ValidatorMessages.FileMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class PurchaseController
{
    private final PurchaseService purchaseService;
    private final UserDetailsServiceImpl userDetailsService;
    private final FileService fileService;

    @Autowired
    PurchaseController(PurchaseService purchaseService, UserDetailsServiceImpl userDetailsService, FileService fileService){
        this.purchaseService = purchaseService;
        this.userDetailsService = userDetailsService;
        this.fileService = fileService;
    }


    /**
     * Добавление сущности закупки
     * Пришлось разложить на Param, т.к. файл нельзя предоставить в json
     *
     * @return ResponseEntity со статусом
     */
    @PostMapping(value = "/purchase", produces = "application/json")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> addPurchase
    (
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "proposalDeadLine") Long proposalDeadLine,
            @RequestParam(value = "finishDeadLine", required = false) Long finishDeadLine,
            @RequestParam(value = "budget", required = false) Long budget,
            @RequestParam(value = "demands", required = false) String demands,
            @RequestParam(value = "team", required = false) String team,
            @RequestParam(value = "workCondition", required = false) String workCondition,
            @RequestParam(value = "files", required = false) MultipartFile[] files) throws FileValidationException
    {
        if (files != null && files.length > 20)
        {
            return new ResponseEntity<>(new ValidateResponse(false, "files", FileMessages.TOO_MUCH_FILES), HttpStatus.NOT_ACCEPTABLE);
        }

        User author = userDetailsService.loadUserByToken(token);

        Purchase objPurchase = new Purchase(author, name, description, proposalDeadLine, finishDeadLine, budget, demands, team, workCondition);
        PurchaseValidate purchaseValidate = new PurchaseValidate(objPurchase);

        ValidateResponse validateResponse = purchaseValidate.validatePurchaseCreate();
        if (!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        fileService.validateFiles(files);
        Purchase validatedPurchase = purchaseValidate.getPurchase();

        try
        {
            Purchase savedPurchase = purchaseService.save(validatedPurchase);
            List<FileEntity> savedFiles = fileService.addFiles(files, savedPurchase.getId(), FileServiceImpl.LOCATION_PURCHASE);
            savedPurchase.setFiles(savedFiles);

            purchaseService.sendEmail(savedPurchase);
            return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Получение страницы с закупками
     *
     * @return ResponseEntity со статусом
     */
    @GetMapping(value = "/purchases", produces = "application/json")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getPurchases(@RequestParam(value = "requestPage", required = false) Integer requestPage,
                                               @RequestParam(value = "numberOfElements", required = false) Integer numberOfElements)
    {
        PurchasesPageRequest purchasesPageRequest = new PurchasesPageRequest(requestPage, numberOfElements);
        PurchasesPageValidate purchasesPageValidate = new PurchasesPageValidate(purchasesPageRequest);
        ValidateResponse validateResponse = purchasesPageValidate.validatePurchasePage();

        if (!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        PurchasesPageRequest validPageRequest = purchasesPageValidate.getPurchasePageRequest();

        Pageable pageable = PageRequest.of(validPageRequest.getRequestPage(), validPageRequest.getNumberOfElements(),
                Sort.by("createDate").descending());

        try
        {
            return new ResponseEntity<>(purchaseService.getAll(pageable), HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Получение страницы с закупками
     *
     * @return ResponseEntity со статусом
     */
    @GetMapping(value = "/purchase/{id}", produces = "application/json")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getPurchase(@PathVariable(name = "id") UUID id)
    {
        if (id == null)
        {
            return new ResponseEntity<>(new ApiResponse(false, "Параметр id не предоставлен"), HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Purchase> searchResult = purchaseService.findById(id);

        if (searchResult.isPresent())
        {
            return new ResponseEntity<>(searchResult.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ApiResponse(false, "Закупка не найдена по id"), HttpStatus.NOT_FOUND);

    }

    /**
     * Изменения параметров сущности закупки
     */
    @PutMapping(value = "/purchase", produces = "application/json")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> changePurchase(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "id") UUID id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "proposalDeadLine", required = false) Long proposalDeadLine,
            @RequestParam(value = "finishDeadLine", required = false) Long finishDeadLine,
            @RequestParam(value = "budget", required = false) Long budget,
            @RequestParam(value = "demands", required = false) String demands,
            @RequestParam(value = "team", required = false) String team,
            @RequestParam(value = "workCondition", required = false) String workCondition,
            @RequestParam(value = "status", required = false) PurchaseStatus status,
            @RequestParam(value = "cancelReason", required = false) String cancelReason,
            @RequestParam(value = "files", required = false) MultipartFile[] files) throws FileValidationException
    {
        if(id == null)
        {
            return new ResponseEntity<>(new ApiResponse(false, "Параметр id не предоставлен"), HttpStatus.NOT_ACCEPTABLE);
        }
        User author = userDetailsService.loadUserByToken(token);

        Optional<Purchase> searchResult = purchaseService.findById(id);

        if (!searchResult.isPresent())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Закупка не найдна по id"), HttpStatus.NOT_ACCEPTABLE);
        }
        Purchase oldPurchase = searchResult.get();

        Purchase objPurchase = new Purchase(id, author, name, description, proposalDeadLine, finishDeadLine, budget, demands, team, workCondition, status, cancelReason);
        PurchaseValidate purchaseValidate = new PurchaseValidate(objPurchase);
        ValidateResponse validateResponse = purchaseValidate.validatePurchaseEdit(oldPurchase);

        if (!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        Purchase validatedPurchase = purchaseValidate.getPurchase();

        if (files != null && files.length + oldPurchase.getFiles().size() > 20)
        {
            return new ResponseEntity<>(new ValidateResponse(false, "files", FileMessages.TOO_MUCH_FILES), HttpStatus.NOT_ACCEPTABLE);
        }
        fileService.validateFiles(files);

        try
        {
            Purchase savedPurchase = purchaseService.save(validatedPurchase);

            List<FileEntity> savedFiles = fileService.addFiles(files, savedPurchase.getId(), FileServiceImpl.LOCATION_PURCHASE);
            List<FileEntity> combinedList = Stream.of(savedFiles, savedPurchase.getFiles()).flatMap(Collection::stream)
                    .collect(Collectors.toList());
            savedPurchase.setFiles(combinedList);

            return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Удаление сущности закупки по id
     *
     * @return ResponseEntity со статусом
     */
    @DeleteMapping(value = "/purchase/{id}", produces = "application/json")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> deletePurchase(@PathVariable(name = "id") UUID id)
    {
        if (id == null)
        {
            return new ResponseEntity<>(new ApiResponse(false, "Параметр id не предоставлен"), HttpStatus.NOT_ACCEPTABLE);
        }

        Optional<Purchase> searchResult = purchaseService.findById(id);

        if (!searchResult.isPresent())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Закупка не найдена по id"), HttpStatus.NOT_FOUND);
        }

        try
        {
            purchaseService.deletePurchase(searchResult.get());
            return new ResponseEntity<>(new ApiResponse(false, "Закупка успешно удалена"), HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
