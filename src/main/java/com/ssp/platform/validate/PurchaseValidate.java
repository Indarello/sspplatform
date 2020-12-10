package com.ssp.platform.validate;

import com.ssp.platform.entity.Purchase;
import com.ssp.platform.entity.User;
import com.ssp.platform.entity.enums.PurchaseStatus;
import com.ssp.platform.response.ValidateResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true) //Временно
@Data
public class PurchaseValidate extends Validator
{
    public static final long ONE_HUNDRED_YEARS = 3155760000L;
    public static final long ONE_HOUR = 3600L;
    public static final long THREE_THOUSAND_YEARS = 32503741200L;
    private Purchase purchase;
    private String checkResult = "ok";
    private boolean foundInvalid = false;

    public PurchaseValidate(Purchase purchase)
    {
        this.purchase = purchase;
        this.checkResult = "ok";
        this.foundInvalid = false;
    }

    public ValidateResponse validatePurchaseCreate()
    {
        validateName();
        if (foundInvalid) return new ValidateResponse(false, "name", checkResult);

        validateDescription();
        if (foundInvalid) return new ValidateResponse(false, "description", checkResult);

        validateProposalDeadLine();
        if (foundInvalid) return new ValidateResponse(false, "proposalDeadLine", checkResult);

        validateFinishDeadLine();
        if (foundInvalid) return new ValidateResponse(false, "finishDeadLine", checkResult);

        validateBudget();
        if (foundInvalid) return new ValidateResponse(false, "budget", checkResult);

        validateDemands();
        if (foundInvalid) return new ValidateResponse(false, "demands", checkResult);

        validateTeam();
        if (foundInvalid) return new ValidateResponse(false, "team", checkResult);

        validateWorkCondition();
        if (foundInvalid) return new ValidateResponse(false, "workCondition", checkResult);

        return new ValidateResponse(true, "", checkResult);
    }

    public ValidateResponse validatePurchaseEdit(Purchase oldPurchase)
    {
        purchase.setCreateDate(oldPurchase.getCreateDate());
        purchase.setFiles(oldPurchase.getFiles());
        purchase.setSupplies(oldPurchase.getSupplies());

        String newStringParam = purchase.getName();
        String oldStringParam = oldPurchase.getName();
        if (newStringParam == null) purchase.setName(oldStringParam);
        else if (!newStringParam.equals(oldStringParam))
        {
            validateName();
            if (foundInvalid) return new ValidateResponse(false, "name", checkResult);
        }

        newStringParam = purchase.getDescription();
        oldStringParam = oldPurchase.getDescription();
        if (newStringParam == null) purchase.setDescription(oldStringParam);
        else if (!newStringParam.equals(oldStringParam))
        {
            validateDescription();
            if (foundInvalid) return new ValidateResponse(false, "description", checkResult);
        }

        Long newLongParam = purchase.getProposalDeadLine();
        Long oldLongParam = oldPurchase.getProposalDeadLine();
        if (newStringParam == null) purchase.setProposalDeadLine(oldLongParam);
        else if (!oldLongParam.equals(newLongParam))
        {
            validateProposalDeadLine();
            if (foundInvalid) return new ValidateResponse(false, "proposalDeadLine", checkResult);
        }

        newLongParam = purchase.getFinishDeadLine();
        oldLongParam = oldPurchase.getFinishDeadLine();
        if (newStringParam == null) purchase.setFinishDeadLine(oldLongParam);
        else if (!oldLongParam.equals(newLongParam))
        {
            validateFinishDeadLine();
            if (foundInvalid) return new ValidateResponse(false, "finishDeadLine", checkResult);
        }

        newLongParam = purchase.getBudget();
        oldLongParam = oldPurchase.getBudget();
        if (newStringParam == null) purchase.setBudget(oldLongParam);
        else if (!oldLongParam.equals(newLongParam))
        {
            validateBudget();
            if (foundInvalid) return new ValidateResponse(false, "budget", checkResult);
        }

        newStringParam = purchase.getDemands();
        oldStringParam = oldPurchase.getDemands();
        if (newStringParam == null) purchase.setDemands(oldStringParam);
        else if (!newStringParam.equals(oldStringParam))
        {
            validateDemands();
            if (foundInvalid) return new ValidateResponse(false, "demands", checkResult);
        }

        newStringParam = purchase.getTeam();
        oldStringParam = oldPurchase.getTeam();
        if (newStringParam == null) purchase.setTeam(oldStringParam);
        else if (!newStringParam.equals(oldStringParam))
        {
            validateTeam();
            if (foundInvalid) return new ValidateResponse(false, "team", checkResult);
        }

        newStringParam = purchase.getWorkCondition();
        oldStringParam = oldPurchase.getWorkCondition();
        if (newStringParam == null) purchase.setWorkCondition(oldStringParam);
        else if (!newStringParam.equals(oldStringParam))
        {
            validateWorkCondition();
            if (foundInvalid) return new ValidateResponse(false, "workCondition", checkResult);
        }

        PurchaseStatus newStatus = purchase.getStatus();
        PurchaseStatus oldStatus = oldPurchase.getStatus();
        if (newStringParam == null) purchase.setStatus(oldStatus);
        else if (!newStatus.equals(oldStatus))
        {
            validateStatus();
            if (foundInvalid) return new ValidateResponse(false, "status", checkResult);
        }

        newStringParam = purchase.getCancelReason();
        oldStringParam = oldPurchase.getCancelReason();
        if (newStringParam == null) purchase.setCancelReason(oldStringParam);
        else if (!newStringParam.equals(oldStringParam))
        {
            validateCancelReason();
            if (foundInvalid) return new ValidateResponse(false, "cancelReason", checkResult);
        }

        return new ValidateResponse(true, "", checkResult);
    }

    private void validateName()
    {
        String checkString = purchase.getName();
        if (checkString == null)
        {
            setCheckResult("Поле наименование закупки должно быть заполнено");
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult("Наименование закупки не может состоять из одних пробелов");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 1 || checkLength > 100)
        {
            setCheckResult("Наименование закупки должно содержать от 1 до 100 символов");
            return;
        }

    }

    private void validateDescription()
    {
        String checkString = purchase.getDescription();
        if (checkString == null)
        {
            setCheckResult("Поле описание закупки должно быть заполнено");
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult("Описание закупки не может состоять из одних пробелов");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 1 || checkLength > 1000)
        {
            setCheckResult("Описание закупки должно содержать от 1 до 1000 символов");
            return;
        }

    }

    private void validateProposalDeadLine()
    {
        Long proposalSec = purchase.getProposalDeadLine();
        if (proposalSec == null)
        {
            setCheckResult("Дата окончания срока подачи предложений должна быть заполнена");
            return;
        }

        long createSec = purchase.getCreateDate();
        if (proposalSec > createSec + ONE_HUNDRED_YEARS)
        {
            setCheckResult("Дата окончания срока подачи предложений " +
                    "должна быть не позже чем через 100 лет от даты создания предложения");
            return;
        }

        if (proposalSec < createSec + ONE_HOUR)
        {
            setCheckResult("Время окончания срока подачи предложений " +
                    "не может быть раньше чем через час после создания предложения");
            return;
        }
    }

    private void validateFinishDeadLine()
    {
        Long finishSec = purchase.getFinishDeadLine();

        if (finishSec == null)
        {
            purchase.setFinishDeadLine(THREE_THOUSAND_YEARS);
            return;
        }
        if (finishSec < purchase.getProposalDeadLine())
        {
            setCheckResult("Дата окончания выполнения работ " +
                    "не может быть перед датой окончания срока подачи предложений");
            return;
        }
    }

    private void validateBudget()
    {
        Long budget = purchase.getBudget();
        if (budget == null)
        {
            purchase.setBudget(0L);
            return;
        }

        if (budget < 0)
        {
            setCheckResult("Бюджет закупки не может быть отрицательный");
            return;
        }

        if (budget > 99999999)
        {
            setCheckResult("Бюджет закупки должен быть не более 8 цифр");
            return;
        }

    }


    private void validateDemands()
    {
        String checkString = purchase.getDemands();
        if (checkString == null)
        {
            purchase.setDemands("");
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult("Общие требования не могут состоять из одних пробелов");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength > 1000)
        {
            setCheckResult("Общие требования должны содержать не более 1000 символов");
            return;
        }
    }

    private void validateTeam()
    {
        String checkString = purchase.getTeam();
        if (checkString == null)
        {
            purchase.setTeam("");
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult("Состав команды не может состоять из одних пробелов");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength > 1000)
        {
            setCheckResult("Состав команды должен содержать не более 1000 символов");
            return;
        }
    }

    private void validateWorkCondition()
    {
        String checkString = purchase.getWorkCondition();
        if (checkString == null)
        {
            purchase.setWorkCondition("");
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult("Условия работы не может состоять из одних пробелов");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength > 1000)
        {
            setCheckResult("Условия работы должны содержать не более 1000 символов");
            return;
        }

    }

    private void validateStatus()
    {
        if (purchase.getStatus() == PurchaseStatus.bidAccepting)
        {
            long nowSec = System.currentTimeMillis() / 1000;
            if (nowSec >= purchase.getProposalDeadLine())
            {
                setCheckResult("Время приема предложений закончено");
                return;
            }
        }
        else if (purchase.getStatus() == PurchaseStatus.bidReview)
        {
            long nowSec = System.currentTimeMillis() / 1000;
            if (nowSec < purchase.getProposalDeadLine())
            {
                setCheckResult("Установите срок подачи предложения меньший текущего времени");
                return;
            }
        }
    }

    private void validateCancelReason()
    {
        String checkString = purchase.getCancelReason();
        if (checkString == null)
        {
            purchase.setCancelReason("");
            return;
        }

        if (onlySpaces(checkString))
        {
            setCheckResult("Причина отмены не может состоять из одних пробелов");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength > 1000)
        {
            setCheckResult("Причина отмены должна содержать не более 1000 символов");
            return;
        }
    }

    private void setCheckResult(String result)
    {
        foundInvalid = true;
        checkResult = result;
    }

}
