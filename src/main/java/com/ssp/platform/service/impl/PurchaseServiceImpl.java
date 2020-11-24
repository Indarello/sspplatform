package com.ssp.platform.service.impl;

import com.ssp.platform.entity.Purchase;
import com.ssp.platform.repository.PurchaseRepository;
import com.ssp.platform.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PurchaseServiceImpl implements PurchaseService
{

    private final PurchaseRepository purchaseRepository;

    @Autowired
    PurchaseServiceImpl(PurchaseRepository purchaseRepository)
    {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase save(Purchase purchase)
    {
        return purchaseRepository.saveAndFlush(purchase);
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
    public boolean deletePurchase(UUID id)
    {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isPresent())
        {
            purchaseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Purchase> findById(UUID id)
    {
        return purchaseRepository.findById(id);
    }
}
