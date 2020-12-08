package com.ssp.platform.validate;

import com.ssp.platform.property.FileProperty;
import com.ssp.platform.response.ValidateResponse;
import com.ssp.platform.validate.ValidatorMessages.FileValidatorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileValidator extends Validator{

    private final String FIELD_NAME = "file";

    private final long MAX_FILE_SIZE = 10485760L;

    private final int MAX_FILENAME_SIZE = 255;

    private final Pattern restrictedRegex;

    @Autowired
    public FileValidator(FileProperty fileProperty)
    {
        String[] restrictedTypes = fileProperty.getRestrictedTypes();
        String regex = ".*(\\.";

        for (String s : restrictedTypes)
        {
            regex = regex.concat(s);
            regex = regex.concat("|\\.");
        }
        regex = regex.substring(0, regex.length() - 3);
        regex = regex.concat(")");
        //System.out.println(regex);
        restrictedRegex = Pattern.compile(regex);


    }

    public ValidateResponse validateFile(MultipartFile file){
        if (file == null){
            return new ValidateResponse(true, "", FileValidatorMessages.OK);
        }

        if (file.getSize() > MAX_FILE_SIZE){
            return new ValidateResponse(false, FIELD_NAME, FileValidatorMessages.WRONG_FILE_SIZE_ERROR);
        }

        String fileName = file.getOriginalFilename();

        if (fileName.length() > MAX_FILENAME_SIZE){
            return new ValidateResponse(false, FIELD_NAME, FileValidatorMessages.WRONG_FILENAME_SIZE_ERROR);
        }

        if(checkRestrictedType(fileName))
        {
            return new ValidateResponse(false, FIELD_NAME, "Файл с таким расширением запрещено загружать");
        }

        return new ValidateResponse(true, FileValidatorMessages.OK);
    }

    private boolean checkRestrictedType(String fileName)
    {
        Matcher matcher = restrictedRegex.matcher(fileName);

        return matcher.matches();
    }

    public ValidateResponse validateFiles(MultipartFile[] files){
        for (MultipartFile file : files){
            ValidateResponse validatorResponse = validateFile(file);
            if (!validatorResponse.isSuccess()) return validatorResponse;
        }

        return new ValidateResponse(true, "", FileValidatorMessages.OK);
    }

}
