package com.ssp.platform.entity.enums;

public enum SupplyStatus {
    UNDER_REVIEW("На рассмотрении"),   //Рассматривается
    DENIED("Отклонено"),             //Отклонено
    WINNER("Победитель");           //Победитель

    private String message;

    SupplyStatus(String status) {
        message = status;
    }

    public String getMessage() {
        return message;
    }
}
