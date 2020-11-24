package com.ssp.platform.controller;

import com.ssp.platform.entity.FileEntity;
import com.ssp.platform.entity.Purchase;
import com.ssp.platform.entity.User;
import com.ssp.platform.response.ApiResponse;
import com.ssp.platform.response.ValidateResponse;
import com.ssp.platform.security.service.UserDetailsServiceImpl;
import com.ssp.platform.service.FileService;
import com.ssp.platform.service.PurchaseService;
import com.ssp.platform.validate.FileValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@ControllerAdvice
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
     * Я не знаю как это по другому решить
     * но без этого если размер файла превышает допустимый (10МБ) - всегда вылетает SizeLimitExceededException
     * при этом на стороне сервера
     * Использую его родительский класс (MultipartException), может быть там еще какие то ошибки бывают
     */
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleMultipartException(MultipartException ex)
    {
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
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
        @RequestParam(value = "proposalDeadLine") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH") Date proposalDeadLine,
        @RequestParam(value = "finishDeadLine", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH") Date finishDeadLine,
        @RequestParam(value = "budget", required = false) BigInteger budget,
        @RequestParam(value = "demands", required = false) String demands,
        @RequestParam(value = "team", required = false) String team,
        @RequestParam(value = "workCondition", required = false) String workCondition,
        @RequestParam(value = "files", required = false) MultipartFile[] files
    )   throws IOException, NoSuchAlgorithmException
    {
        User author = userDetailsService.loadUserByToken(token);
        Purchase validatedPurchase = new Purchase(author, name, description, proposalDeadLine,
                finishDeadLine, budget, demands, team, workCondition);

        //System.out.println("123");

        //TODO validate
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
                savedFiles = fileService.addFiles(files);
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
            return new ResponseEntity<>(purchaseService.save(savedPurchase), HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Получение страницы с закупками
     *
     * @return ResponseEntity со статусом
     */
    @GetMapping(value = "/purchases", produces = "application/json")
    @PreAuthorize("hasAuthority('employee') or hasAuthority('firm')")
    public ResponseEntity<Object> getPurchases(@RequestParam("requestPage") int requestPage,
                                               @RequestParam("numberOfElements") int numberOfElements)
    {
        //TODO validate когда будет еще фильтрация и сортировка
        if (requestPage < 0 || requestPage > 100_000)
        {
            return new ResponseEntity<>(new ApiResponse(false,
                    "Parameter requestPage can be only 0-100000"), HttpStatus.NOT_ACCEPTABLE);
        }

        if (numberOfElements < 1 || numberOfElements > 100)
        {
            numberOfElements = 10;
        }

        Pageable pageable = PageRequest.of(requestPage, numberOfElements);

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
     *
     * @return ResponseEntity со статусом
     */
    @PutMapping(value = "/purchase", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority('employee')")
    public ResponseEntity<Object> changePurchase(@RequestBody Purchase purchase)
    {

        if (purchase.getId().toString().isEmpty())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Empty id"),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (purchase.getName().isEmpty())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Empty name"),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (purchase.getAuthor().getUsername().isEmpty())
        {
            return new ResponseEntity<>(new ApiResponse(false, "Empty author field"),
                    HttpStatus.NOT_ACCEPTABLE);
        }

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
