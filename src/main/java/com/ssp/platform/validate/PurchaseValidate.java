package com.ssp.platform.validate;

import com.ssp.platform.entity.Purchase;
import com.ssp.platform.response.ValidateResponse;
import lombok.Data;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class PurchaseValidate
{
    private Purchase purchase;
    private String checkResult = "ok";
    private boolean foundInvalid = false;

    PurchaseValidate(Purchase purchase)
    {
        this.purchase = purchase;
        this.checkResult = "ok";
        this.foundInvalid = false;
    }

    public ValidateResponse validateRegistrationPurchase()
    {
        validateName();
        if (foundInvalid) return new ValidateResponse(false, "name", checkResult);

        //TODO:Проверки на  null добавь, в необязательных параметрах выставишь ""как в шаблоне
        //В обязательных вывод ошибки
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

        validateStatus();
        if (foundInvalid) return new ValidateResponse(false, "status", checkResult);

        validateCancelReason();
        if (foundInvalid) return new ValidateResponse(false, "cancelReason", checkResult);

        return new ValidateResponse(true, "", checkResult);
    }

    private void validateName()
    {
        String checkString = purchase.getName();
        if (checkString == null)
        {
            setCheckResult("Параметр name не предоставлен");
            return;
        }

        int checkLength = checkString.length();

        if (checkLength < 1 || checkLength > 100)
        {
            setCheckResult("Параметр name должен быть от 1 до 100 символов");
            return;
        }

        //TODO: должно содержать хотя бы 1 букву (кирилица/латиница)
    }

    private void validateDescription()
    {
        String checkString = purchase.getDescription();
        if (checkString == null)
        {
            setCheckResult("Параметр description не предоставлен");
            return;
        }

        int checkLength = checkString.length();

        if (checkLength < 1 || checkLength > 100)
        {
            setCheckResult("Параметр description должен быть от 1 до 100 символов");
            return;
        }

        //TODO: должно содержать хотя бы 1 букву (кирилица/латиница)
    }

    private void validateProposalDeadLine()
    {
        Date date = purchase.getProposalDeadLine();
        if (date.toString().isEmpty())
        {
            return setCheckResult("Дата и время окончания срока подачи предложений" +
                    "не может быть неуказанной!");
        }
        if (date.before(purchase.getCreateDate()))
        {
            return setCheckResult("Дата окончания срока подачи предложений " +
                    "не может быть перед датой создания предложения");
        }
        if (date.after(purchase.getFinishDeadLine()))
        {
            return setCheckResult("Дата окончания срока подачи предложений " +
                    "не может быть после даты окончания выполнения работ");
        }

    }

    private void validateFinishDeadLine()
    {
        Date date = purchase.getFinishDeadLine();
        if (date.before(purchase.getCreateDate()))
        {
            return setCheckResult("Дата окончания выполнения работ " +
                    "не может быть перед датой создания предложения");
        }
        if (date.before(purchase.getProposalDeadLine()))
        {
            return setCheckResult("Дата окончания выполнения работ " +
                    "не может быть перед датой окончания срока подачи предложений");
        }

    }

    private void validateBudget()
    {
        if (!inBounds(purchase.getBudget().toString(), 1, 8))
        {
            return setCheckResult("Параметр budge неподходящего размера! " +
                    "Бюджет может быть от 1 до 8 символов)");
        }

    }

    private void validateTeam()
    {
        if (!inBounds(purchase.getTeam(), 1, 100))
        {
            return setCheckResult("Параметр team неподходящего размера! " +
                    "Поле команды может быть от 1 до 100 символов)");
        }

    }

    private void validateDemands()
    {
        if (!inBounds(purchase.getDemands(), 1, 1000))
        {
            return setCheckResult("Параметр demands неподходящего размера! " +
                    "Поле общих требований может быть от 1 до 1000 символов)");
        }

    }

    private void validateWorkCondition()
    {
        if (!inBounds(purchase.getWorkCondition(), 1, 100))
        {
            return setCheckResult("Параметр workCondition неподходящего размера! " +
                    "Поле условия работы может быть от 1 до 100 символов)");
        }

    }

    private void validateStatus()
    {
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
        if (!inBounds(purchase.getCancelReason(), 1, 100))
        {
            return setCheckResult("Параметр cancelReason неподходящего размера! " +
                    "Поле причины отмены может быть от 1 до 100 символов)");
        }

    }

    private void setCheckResult(String result)
    {
        foundInvalid = true;
        checkResult = result;
    }

    private boolean isMatch(String field, String regex)
    {
        if (field.isEmpty())
        {
            return true;
        }

        Pattern fieldPattern = Pattern.compile(regex);
        Matcher matcher = fieldPattern.matcher(field);

        return matcher.matches();
    }

}
