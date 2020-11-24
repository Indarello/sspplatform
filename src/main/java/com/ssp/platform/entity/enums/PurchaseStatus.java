package com.ssp.platform.entity.enums;

public enum PurchaseStatus
{
    //STARTED("начата"),
    //CANCELED("отменена"),
    //FINISHED("сделано"),

    bidAccepting("принятие заявок на участие"),
    bidReview("рассмотрение заявок"),
    canceled("отменена"),
    finished("завершена");

    public String message;

    PurchaseStatus(String status)
    {
        this.message = status;
    }

    public String getMessage()
    {
        return this.message;
    }
}