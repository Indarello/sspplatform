package com.ssp.platform.service.impl;

import com.ssp.platform.entity.FileEntity;
import com.ssp.platform.entity.Purchase;
import com.ssp.platform.entity.SupplyEntity;
import com.ssp.platform.entity.enums.PurchaseStatus;
import com.ssp.platform.exceptions.SupplyException;
import com.ssp.platform.repository.PurchaseRepository;
import com.ssp.platform.service.FileService;
import com.ssp.platform.service.PurchaseService;
import com.ssp.platform.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableScheduling
public class PurchaseServiceImpl implements PurchaseService
{
    private final FileService fileService;
    private final PurchaseRepository purchaseRepository;
    private final SupplyService supplyService;

    @Autowired
    PurchaseServiceImpl(FileService fileService, PurchaseRepository purchaseRepository, SupplyService supplyService)
    {
        this.fileService = fileService;
        this.purchaseRepository = purchaseRepository;
        this.supplyService = supplyService;
    }

    @Override
    public Purchase save(Purchase purchase)
    {
        return purchaseRepository.saveAndFlush(purchase);
    }

    @Override
    public Purchase get(UUID id)
    {
        return purchaseRepository.getOne(id);
    }

    @Override
    public Page<Purchase> getAll(Pageable pageable)
    {
        return purchaseRepository.findAll(pageable);
    }

    @Override
    public Optional<Purchase> changePurchase(Purchase purchase)
    {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(purchase.getId());
        if (optionalPurchase.isPresent())
        {
            return Optional.of(purchaseRepository.saveAndFlush(purchase));
        }
        return Optional.empty();
    }

    @Override
    public boolean deletePurchase(Purchase purchase) throws IOException, SupplyException
    {
        List<SupplyEntity> supplies = purchase.getSupplies();
        for (SupplyEntity supply : supplies)
        {
            supplyService.delete(supply.getId());
        }

        List<FileEntity> files = purchase.getFiles();
        for (FileEntity file : files)
        {
            fileService.delete(file.getId());
        }

        purchaseRepository.delete(purchase);
        return true;
    }

    @Override
    public Optional<Purchase> findById(UUID id)
    {
        return purchaseRepository.findById(id);
    }

    @Override
    public boolean existById(UUID id)
    {
        return purchaseRepository.existsById(id);
    }

    @Scheduled(fixedDelay = 60000)
    public void updateStatus()
    {
        List<Purchase> purchases = purchaseRepository.findByStatusOrStatus(PurchaseStatus.bidAccepting, PurchaseStatus.bidReview);
        long nowSec = System.currentTimeMillis() / 1000;
        for (Purchase purchase : purchases)
        {
            PurchaseStatus oldStatus = purchase.getStatus();

            if (nowSec >= purchase.getFinishDeadLine())
            {
                purchase.setStatus(PurchaseStatus.finished);
                purchaseRepository.save(purchase);
            }
            else if (oldStatus == PurchaseStatus.bidAccepting && nowSec >= purchase.getProposalDeadLine())
            {
                purchase.setStatus(PurchaseStatus.bidReview);
                purchaseRepository.save(purchase);
            }
        }
    }
}
