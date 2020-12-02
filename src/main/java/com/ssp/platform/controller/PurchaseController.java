package com.ssp.platform.controller;

import com.ssp.platform.entity.FileEntity;
import com.ssp.platform.entity.Purchase;
import com.ssp.platform.entity.User;
import com.ssp.platform.request.PurchasesPageRequest;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.ValidateResponse;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.FileService;
import com.ssp.platform.service.PurchaseService;
import com.ssp.platform.validate.FileValidate;
import com.ssp.platform.validate.PurchaseValidate;
import com.ssp.platform.validate.PurchasesPageValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PurchaseController
{
    private final PurchaseService purchaseService;
    private final UserDetailsServiceImpl userDetailsService;
    private final FileService fileService;

    @Autowired
    PurchaseController(PurchaseService purchaseService, UserDetailsServiceImpl userDetailsService, FileService fileService)
    {
        this.purchaseService = purchaseService;
        this.userDetailsService = userDetailsService;
        this.fileService = fileService;
    }


    /**
     * Добавление сущности закупки
     * Пришлось разложить на Param, т.к. файл нельзя предоставить в json
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
        @RequestParam(value = "budget", required = false) BigInteger budget,
        @RequestParam(value = "demands", required = false) String demands,
        @RequestParam(value = "team", required = false) String team,
        @RequestParam(value = "workCondition", required = false) String workCondition,
        @RequestParam(value = "files", required = false) MultipartFile[] files
    )   throws IOException, NoSuchAlgorithmException
    {
        User author = userDetailsService.loadUserByToken(token);

        Purchase objPurchase = new Purchase(author, name, description, proposalDeadLine,
                finishDeadLine, budget, demands, team, workCondition);

        PurchaseValidate purchaseValidate = new PurchaseValidate(objPurchase);

        ValidateResponse validateResponse = purchaseValidate.validatePurchaseCreate();
        if(!validateResponse.isSuccess())
        {
            return new ResponseEntity<>(validateResponse, HttpStatus.NOT_ACCEPTABLE);
        }

        Purchase validatedPurchase = purchaseValidate.getPurchase();


        FileEntity savedFiles = null;

        if (files != null)
        {
            ValidateResponse validateResult = FileValidate.validateFiles(files);
            if (!validateResult.isSuccess())
            {
                if(validateResult.getField().equals("size")) savedFiles = null;

                else return new ResponseEntity<>(validateResult, HttpStatus.NOT_ACCEPTABLE);
            }
            else
            {
                //TODO доделать несколько файлов
                savedFiles = fileService.addFile(files[0]);
                validatedPurchase.setFile(savedFiles);
            }
        }

        Purchase savedPurchase = purchaseService.save(validatedPurchase);

        if(savedFiles != null)
        {
            savedFiles.setPurchase(savedPurchase);
            fileService.save(savedFiles);
        }

        try
        {
            //TODO: отправить приглашение на email
            return new ResponseEntity<>(purchaseService.save(savedPurchase), HttpStatus.CREATED);
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

        if(!validateResponse.isSuccess())
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
    @PutMapping(value = "/purchase", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> changePurchase(@RequestHeader("Authorization") String token, @RequestBody Purchase purchase)
    {
        User author = userDetailsService.loadUserByToken(token);
        purchase.setAuthor(author);
        try
        {
            Optional<Purchase> editResult = purchaseService.changePurchase(purchase);
            if (editResult.isPresent())
            {
                return new ResponseEntity<>(editResult.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(false, "Purchase not found"), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<Object> deletePurchase(@PathVariable(name = "id") String id)
    {
        if (id.isEmpty())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Empty id"), HttpStatus.NOT_ACCEPTABLE);
        }

        try
        {
            if (purchaseService.deletePurchase(UUID.fromString(id)))
            {
                return new ResponseEntity<>(new ApiResponse(true, "List deleted"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse(false, "List not found"), HttpStatus.NOT_FOUND);

        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
