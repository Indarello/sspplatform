package com.ssp.platform.validate;

import com.ssp.platform.entity.Purchase;
import com.ssp.platform.response.ValidateResponse;
import lombok.Data;
import java.math.BigInteger;

@Data
public class PurchaseValidate
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

    private void validateName()
    {
        String checkString = purchase.getName();
        if (checkString == null)
        {
            setCheckResult("Поле наименование закупки должно быть заполнено!");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 1 || checkLength > 100)
        {
            setCheckResult("Наименование закупки должно содержать от 1 до 100 символов!");
            return;
        }

    }

    private void validateDescription()
    {
        String checkString = purchase.getDescription();
        if (checkString == null)
        {
            setCheckResult("Поле описание закупки должно быть заполнено!");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength < 1 || checkLength > 1000)
        {
            setCheckResult("Описание закупки должно содержать от 1 до 1000 символов!");
            return;
        }

    }

    private void validateProposalDeadLine()
    {
        Long proposalSec = purchase.getProposalDeadLine();
        if (proposalSec == null)
        {
            setCheckResult("Дата окончания срока подачи предложений должна быть заполнена!");
            return;
        }

        long nowSec = System.currentTimeMillis()/1000;
        if (proposalSec > nowSec + ONE_HUNDRED_YEARS)
        {
            setCheckResult("Дата окончания срока подачи предложений " +
                    "должна быть не позже чем через 100 лет от текущей даты.");
            return;
        }

        if (proposalSec < nowSec + ONE_HOUR)
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
        BigInteger budget = purchase.getBudget();
        if (budget == null)
        {
            purchase.setBudget(BigInteger.ZERO);
            return;
        }

        int checkLength = budget.toString().length();
        if (checkLength > 8)
        {
            setCheckResult("Бюджет закупки должен быть не более 8 цифр!");
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

        int checkLength = checkString.length();
        if (checkLength > 1000)
        {
            setCheckResult("Общие требования должны содержать не более 1000 символов!");
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

        int checkLength = checkString.length();
        if (checkLength > 100)
        {
            setCheckResult("Состав команды должен содержать не более 100 символов!");
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

        int checkLength = checkString.length();
        if (checkLength > 100)
        {
            setCheckResult("Условия работы должны содержать не более 100 символов!");
            return;
        }

    }

    private void validateStatus()
    {
		/*
		Status status = purchase.getStatus();
		if (status == null){
			setCheckResult("Параметр status не предоставлен");
			return;
		}

		 */
//		Status status = purchase.getStatus();
//		if (status.getMessage().equals("начата") ||
//				status.getMessage().equals("отменена")||
//				status.getMessage().equals("сделано")){
//			return "ok";
//		}

//		return setCheckResult("Неизвестный статус! Статусы: начата, отменена, сделано");
    }

    private void validateCancelReason()
    {
        String checkString = purchase.getCancelReason();
        if (checkString == null)
        {
            purchase.setCancelReason("");
            return;
        }

        int checkLength = checkString.length();
        if (checkLength > 100)
        {
            //Пока до конца не известно
            setCheckResult("Причина отмены должна содержать не более 100 символов!");
            return;
        }
    }

    private void setCheckResult(String result)
    {
        foundInvalid = true;
        checkResult = result;
    }

}
