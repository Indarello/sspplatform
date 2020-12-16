package com.ssp.platform.service.impl;

import com.ssp.platform.entity.FileEntity;
import com.ssp.platform.entity.Purchase;
import com.ssp.platform.entity.SupplyEntity;
import com.ssp.platform.entity.User;
import com.ssp.platform.entity.enums.PurchaseStatus;
import com.ssp.platform.exceptions.FileServiceException;
import com.ssp.platform.exceptions.SupplyServiceException;
import com.ssp.platform.property.EmailAnnouncementProperty;
import com.ssp.platform.repository.PurchaseRepository;
import com.ssp.platform.service.FileService;
import com.ssp.platform.service.PurchaseService;
import com.ssp.platform.service.SupplyService;
import com.ssp.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
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
    private final UserService userService;
    private final MailServiceImpl mailServiceImpl;

    private final int emailStatus;
    private final String emailHost;
    private final String emailHeading;
    private final String emailFirstLine;
    private final int emailDescription;
    private final int emailBudget;

    @Autowired
    PurchaseServiceImpl(FileService fileService, PurchaseRepository purchaseRepository, SupplyService supplyService,
                        UserService userService, EmailAnnouncementProperty emailAnnouncementProperty, MailServiceImpl mailServiceImpl)
    {
        this.fileService = fileService;
        this.purchaseRepository = purchaseRepository;
        this.supplyService = supplyService;
        this.userService = userService;
        this.emailStatus = emailAnnouncementProperty.getStatus();
        this.emailHost = emailAnnouncementProperty.getHost();
        this.emailHeading = emailAnnouncementProperty.getHeading();
        this.emailFirstLine = emailAnnouncementProperty.getFirstLine();
        this.emailDescription = emailAnnouncementProperty.getDescription();
        this.emailBudget = emailAnnouncementProperty.getBudget();
        this.mailServiceImpl = mailServiceImpl;
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
    public boolean deletePurchase(Purchase purchase) throws IOException, FileServiceException, SupplyServiceException
    {
        List<SupplyEntity> supplies = purchase.getSupplies();
        for (SupplyEntity supply : supplies)
        {
            supplyService.delete(purchase.getAuthor(), supply.getId());
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

    @Override
    public void sendEmail(Purchase purchase)
    {
        if (emailStatus == 0) return;

        String message = emailFirstLine + "<br>";
        message = message + "Название закупки: " + purchase.getName() + "<br>";
        if (emailDescription == 1)
        {
            message = message + "Описание закупки: " + purchase.getDescription() + "<br>";
        }
        if (emailBudget == 1)
        {
            Long budget = purchase.getBudget();
            if (budget > 0) message = message + "Бюджет закупки: " + budget + "<br>";
        }

        message = message + "Закупка доступна по адресу: " + emailHost + "/purchase/" + purchase.getId();

        List<User> users = userService.findByRoleAndStatus("firm", "Approved");

        Date nowDate = new Date();

        try
        {
            mailServiceImpl.sendMailPurchase(emailHeading, message, users, nowDate);
        }
        catch (MessagingException | IOException e)
        {
            //TODO логирование в
            e.printStackTrace();
        }
    }
}
