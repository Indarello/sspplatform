package com.ssp.platform.validate;

import com.ssp.platform.response.ValidatorResponse;
import com.ssp.platform.validate.ValidatorMessages.FileValidatorMessages;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator extends Validator{

    private final String FIELD_NAME = "file";

    private final long MAX_FILE_SIZE = 10485760L;

    private final int MAX_FILENAME_SIZE = 255;

    public ValidatorResponse validateFile(MultipartFile file){
        if (file == null){
            return new ValidatorResponse(true, FileValidatorMessages.OK);
        }

        if (file.getSize() > MAX_FILE_SIZE){
            return new ValidatorResponse(false, FIELD_NAME, FileValidatorMessages.WRONG_FILE_SIZE_ERROR);
        }

        if (file.getOriginalFilename().length() > MAX_FILENAME_SIZE){
            return new ValidatorResponse(false, FIELD_NAME, FileValidatorMessages.WRONG_FILENAME_SIZE_ERROR);
        }

        /**if (!isMatch(file.getOriginalFilename(), "([а-яА-Яa-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.rar|.zip)$")){
            return new ValidatorResponse(false, FIELD_NAME, ValidatorMessages.WRONG_FILE_TYPE_ERROR);
        }*/

        return new ValidatorResponse(true, FileValidatorMessages.OK);
    }

    public ValidatorResponse validateFiles(MultipartFile[] files){
        for (MultipartFile file : files){
            ValidatorResponse validatorResponse = validateFile(file);
            if (!validatorResponse.isSuccess()) return validatorResponse;
        }

        return new ValidatorResponse(true, FileValidatorMessages.OK);
    }

}
