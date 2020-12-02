package com.ssp.platform.validate;

import com.ssp.platform.entity.SupplyEntity;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.response.ValidatorResponse;
import com.ssp.platform.service.impl.PurchaseServiceImpl;
import com.ssp.platform.validate.ValidatorMessages.SupplyValidatorMessages;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.ssp.platform.validate.ValidatorMessages.SupplyValidatorMessages.*;

@Component
public class SupplyValidator extends Validator {
    public static final int ROLE_EMPLOYEE = 0;
    public static final int ROLE_FIRM = 1;

    private final int MAX_SUPPLY_DESCRIPTION_SYMBOLS = 1000;
    private final int MAX_SUPPLY_COMMENT_SYMBOLS = 1000;
    private final int MAX_SUPPLY_RESULT_SYMBOLS = 1000;
    private final long MAX_SUPPLY_BUDGET = 99999999L;

    final String PURCHASE_ID_FIELD_NAME = "purchaseId";
    final String DESCRIPTION_FIELD_NAME = "description";
    final String BUDGET_FIELD_NAME = "budget";
    final String COMMENT_FIELD_NAME = "comment";
    final String STATUS_FIELD_NAME = "status";
    final String RESULT_FIELD_NAME = "review_result";

    private String checkResult = OK;
    private boolean foundInvalid = false;

    private final PurchaseServiceImpl purchaseService;

    public SupplyValidator(PurchaseServiceImpl purchaseService) {
        this.purchaseService = purchaseService;
    }

    public ValidatorResponse validateSupplyCreating(SupplyEntity supplyEntity){
        validatePurchaseId(supplyEntity.getPurchase().getId());
        if (foundInvalid) return new ValidatorResponse(false, PURCHASE_ID_FIELD_NAME, checkResult);

        validateDescription(supplyEntity.getDescription());
        if (foundInvalid) return new ValidatorResponse(false, DESCRIPTION_FIELD_NAME, checkResult);

        validateBudget(supplyEntity.getBudget());
        if (foundInvalid) return new ValidatorResponse(false, BUDGET_FIELD_NAME, checkResult);

        validateComment(supplyEntity.getComment());
        if (foundInvalid) return new ValidatorResponse(false, COMMENT_FIELD_NAME, checkResult);

        return new ValidatorResponse(true, checkResult);
    }

    public ValidatorResponse validateSupplyUpdating(SupplyEntity updatedEntity, int role) {

        switch (role){
            case ROLE_FIRM:
                validateDescription(updatedEntity.getDescription());
                if (foundInvalid) return new ValidatorResponse(false, DESCRIPTION_FIELD_NAME, checkResult);

                validateBudget(updatedEntity.getBudget());
                if (foundInvalid) return new ValidatorResponse(false, BUDGET_FIELD_NAME, checkResult);

                validateComment(updatedEntity.getComment());
                if (foundInvalid) return new ValidatorResponse(false, COMMENT_FIELD_NAME, checkResult);

                break;

            case ROLE_EMPLOYEE:
                validateStatus(updatedEntity.getStatus());
                if (foundInvalid) return new ValidatorResponse(false, STATUS_FIELD_NAME, checkResult);

                validateResult(updatedEntity.getResultOfConsideration());
                if (foundInvalid) return new ValidatorResponse(false, RESULT_FIELD_NAME, checkResult);

                break;
        }

        return new ValidatorResponse(true, checkResult);
    }

    private void validatePurchaseId(UUID id){
        if (!purchaseService.existById(id)){
            setCheckResult( WRONG_PURCHASE_ID_ERROR);
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            setCheckResult(EMPTY_SUPPLY_DESCRIPTION_ERROR);
            return;
        }

        if (description.isEmpty()) {
            setCheckResult(EMPTY_SUPPLY_DESCRIPTION_ERROR);
            return;
        }

        if (onlySpaces(description)) {
            setCheckResult(ONLY_SPACES_ERROR);
            return;
        }

        if (description.length() > MAX_SUPPLY_DESCRIPTION_SYMBOLS) {
            setCheckResult(WRONG_SUPPLY_DESCRIPTION_BOUNDS_ERROR);
        }
    }

    private void validateBudget(Long budget) {
        if (budget == null){
            return;
        }

        if (budget > MAX_SUPPLY_BUDGET){
            setCheckResult(WRONG_SUPPLY_BUDGET_BOUNDS_ERROR);
            return;
        }

        if (budget < 0L){
            setCheckResult(NEGATIVE_SUPPLY_BUDGET_ERROR);
        }
    }

    private void validateComment(String comment){
        if (comment == null){
            return;
        }

        if (comment.isEmpty()){
            return;
        }

        if (onlySpaces(comment)){
            setCheckResult(ONLY_SPACES_ERROR);
            return;
        }

        if (comment.length() > MAX_SUPPLY_COMMENT_SYMBOLS){
            setCheckResult(WRONG_SUPPLY_COMMENT_BOUNDS_ERROR);
        }
    }

    private void validateStatus(SupplyStatus status){
        if (status == null){
            setCheckResult(EMPTY_SUPPLY_STATUS_ERROR);
        }
    }

    private void validateResult(String result){
        if (result == null){
            setCheckResult(EMPTY_SUPPLY_RESULT_ERROR);
            return;
        }
        
        if (result.isEmpty()){
            setCheckResult(EMPTY_SUPPLY_RESULT_ERROR);
            return;
        }
        
        if (onlySpaces(result)){
            setCheckResult(ONLY_SPACES_ERROR);
            return;
        }
        
        if (result.length() > MAX_SUPPLY_RESULT_SYMBOLS){
            setCheckResult(WRONG_SUPPLY_RESULT_BOUNDS_ERROR);
        }
    }

    private void setCheckResult(String result){
        foundInvalid = true;
        checkResult = result;
    }

}
