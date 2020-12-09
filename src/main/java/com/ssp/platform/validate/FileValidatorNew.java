package com.ssp.platform.validate;

import com.ssp.platform.exceptions.FileValidationException;
import com.ssp.platform.property.FileProperty;
import com.ssp.platform.response.ValidateResponse;
import com.ssp.platform.validate.ValidatorMessages.FileValidatorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileValidatorNew extends Validator{
    public static final int MAX_FILES = 20;

    private static final String FIELD_NAME = "file";

    private static final int MAX_FILENAME_SIZE = 255;
    private static final long MAX_FILE_SIZE = 10485760L;

    private final Pattern restrictedRegex;

    @Autowired
    public FileValidatorNew(FileProperty fileProperty) {
        String[] restrictedTypes = fileProperty.getRestrictedTypes();
        String regex = ".*(\\.";

        for (String type : restrictedTypes) {
            regex = regex.concat(type);
            regex = regex.concat("|\\.");
        }
        regex = regex.substring(0, regex.length() - 3);
        regex = regex.concat(")");

        restrictedRegex = Pattern.compile(regex);
    }

    public void validateFiles(MultipartFile[] files) throws FileValidationException {
        if (files == null) return;
        if (files.length == 0) return;

        if (files.length > MAX_FILES) throw new FileValidationException(new ValidateResponse(false, FIELD_NAME, FileValidatorMessages.TOO_MUCH_FILES));

        for (MultipartFile file : files) validateFile(file);
    }

    public void validateFile(MultipartFile file) throws FileValidationException {
        if (file == null){
            return;
        }

        if (file.getSize() > MAX_FILE_SIZE){
            throw new FileValidationException(new ValidateResponse(false, FIELD_NAME, FileValidatorMessages.WRONG_FILE_SIZE_ERROR));
        }

        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.length() > MAX_FILENAME_SIZE){
            throw new FileValidationException(new ValidateResponse(false, FIELD_NAME, FileValidatorMessages.WRONG_FILENAME_SIZE_ERROR));
        }

        if(checkRestrictedType(fileName)) {
            throw new FileValidationException(new ValidateResponse(false, FIELD_NAME, FileValidatorMessages.WRONG_FILE_TYPE_ERROR));
        }
    }

    private boolean checkRestrictedType(String fileName) {
        Matcher matcher = restrictedRegex.matcher(fileName);

        return matcher.matches();
    }

}
