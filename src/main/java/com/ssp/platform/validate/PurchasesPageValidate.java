package com.ssp.platform.validate;

import com.ssp.platform.request.PurchasesPageRequest;
import com.ssp.platform.response.ValidateResponse;
import lombok.Data;

@Data
public class PurchasesPageValidate
{
    private PurchasesPageRequest purchasePageRequest;

    public PurchasesPageValidate(PurchasesPageRequest purchasePageRequest)
    {
        this.purchasePageRequest = purchasePageRequest;
    }

    public ValidateResponse validatePurchasePage()
    {
        Integer checkInt = purchasePageRequest.getRequestPage();
        if (checkInt == null)
        {
            purchasePageRequest.setRequestPage(0);
        }
        else if (checkInt < 0 || checkInt > 100000)
        {
            return new ValidateResponse(false, "requestPage", "Параметр requestPage может быть только 0-100000");
        }


        checkInt = purchasePageRequest.getNumberOfElements();
        if (checkInt == null)
        {
            purchasePageRequest.setNumberOfElements(10);
        }
        else if (checkInt < 1 || checkInt > 100)
        {
            purchasePageRequest.setNumberOfElements(10);
        }

        return new ValidateResponse(true, "", "ok");
    }
}