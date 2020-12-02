package com.ssp.platform.validate;

import com.ssp.platform.entity.SupplyEntity;
import com.ssp.platform.entity.enums.SupplyStatus;
import com.ssp.platform.response.ValidatorResponse;
import com.ssp.platform.service.impl.PurchaseServiceImpl;
import com.ssp.platform.validate.ValidatorMessages.SupplyValidatorMessages;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SupplyValidator extends Validator {

    public static final int ROLE_EMPLOYEE = 0;

    public static final int ROLE_FIRM = 1;

    private final int MAX_SUPPLY_DESCRIPTION_SYMBOLS = 1000;

    public static final long MAX_SUPPLY_BUDGET = 99999999L;

    private final int MAX_SUPPLY_COMMENT_SYMBOLS = 1000;

    private final int MAX_SUPPLY_RESULT_SYMBOLS = 1000;

    private final PurchaseServiceImpl purchaseService;

    private ValidatorResponse response;

    public SupplyValidator(PurchaseServiceImpl purchaseService) {
        this.purchaseService = purchaseService;
    }

    public ValidatorResponse validateSupplyCreating(SupplyEntity supplyEntity){
        response = new ValidatorResponse(true, SupplyValidatorMessages.OK);

        if (!validatePurchaseId(supplyEntity.getPurchase().getId())){
            return response;
        }

        if (!validateDescription(supplyEntity.getDescription())){
            return response;
        }

        if (!validateBudget(supplyEntity.getBudget())){
            return response;
        }

        validateComment(supplyEntity.getComment());

        return response;
    }

    public ValidatorResponse validateSupplyUpdating(SupplyEntity updatedEntity, int role) {
        response = new ValidatorResponse(true, SupplyValidatorMessages.OK);

        switch (role){
            case ROLE_FIRM:
                if (!validateDescription(updatedEntity.getDescription())){
                    return response;
                }

                if (!validateBudget(updatedEntity.getBudget())){
                    return response;
                }

                if (!validateComment(updatedEntity.getComment())){
                    return response;
                }
                break;

            case ROLE_EMPLOYEE:
                if (!validateStatus(updatedEntity.getStatus())){
                    return response;
                }
                
                if (!validateResult(updatedEntity.getResultOfConsideration())){
                    return response;
                }
                break;
        }

        return response;
    }

    private boolean validatePurchaseId(UUID id){
        final String FIELD_NAME = "purchaseId";

        if (!purchaseService.existById(id)){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.WRONG_PURCHASE_ID_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateDescription(String description) {
        final String FIELD_NAME = "description";

        if (description == null) {
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.EMPTY_SUPPLY_DESCRIPTION_ERROR);
            return false;
        }

        if (description.isEmpty()) {
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.EMPTY_SUPPLY_DESCRIPTION_ERROR);
            return false;
        }

        if (onlySpaces(description)) {
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (description.length() > MAX_SUPPLY_DESCRIPTION_SYMBOLS) {
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.WRONG_SUPPLY_DESCRIPTION_BOUNDS_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateBudget(Long budget) {
        final String FIELD_NAME = "budget";

        if (budget == null){
            return true;
        }

        if (budget > MAX_SUPPLY_BUDGET){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.WRONG_SUPPLY_BUDGET_BOUNDS_ERROR);
            return false;
        }

        if (budget < 0L){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.NEGATIVE_SUPPLY_BUDGET_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateComment(String comment){
        final String FIELD_NAME = "comment";

        if (comment == null){
            return true;
        }

        if (comment.isEmpty()){
            return true;
        }

        if (onlySpaces(comment)){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }

        if (comment.length() > MAX_SUPPLY_COMMENT_SYMBOLS){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.WRONG_SUPPLY_COMMENT_BOUNDS_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateStatus(SupplyStatus status){
        final String FIELD_NAME = "status";

        if (status == null){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.EMPTY_SUPPLY_STATUS_ERROR);
            return false;
        }

        return true;
    }

    private boolean validateResult(String result){
        final String FIELD_NAME = "resultOfConsideration";
        
        if (result == null){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.EMPTY_SUPPLY_RESULT_ERROR);
            return false;
        }
        
        if (result.isEmpty()){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.EMPTY_SUPPLY_RESULT_ERROR);
            return false;
        }
        
        if (onlySpaces(result)){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.ONLY_SPACES_ERROR);
            return false;
        }
        
        if (result.length() > MAX_SUPPLY_RESULT_SYMBOLS){
            response = new ValidatorResponse(false, FIELD_NAME, SupplyValidatorMessages.WRONG_SUPPLY_RESULT_BOUNDS_ERROR);
            return false;
        }

        return true;
    }

}
