package com.ssp.platform.validate;

import com.ssp.platform.response.ValidateResponse;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileValidate
{
    /**
     * Валидация при сохранении файлов на сервер
     */
    public static ValidateResponse validateFiles(MultipartFile[] files)
    {
        if (files.length > 1)
        {
            return new ValidateResponse(false, "files", "Файл может быть только один");
        }
        else if(files.length < 1) return new ValidateResponse(false, "size", "");

        for (MultipartFile file : files)
        {
            String fileName = file.getOriginalFilename();

            if(file.getSize() == 0)
            {
                return new ValidateResponse(false, "size", "");
            }

            if (fileName == null || fileName.length() < 5)
            {
                return new ValidateResponse(false, "files", "Формат файла должен быть zip или rar, имя не короче 1 символа");
            }

            if (fileName.length() > 1000)
            {
                return new ValidateResponse(false, "files", "Слишком длинное название файла");
            }

            String fileType = fileName.substring(fileName.length() - 4);
            //Когда будет больше типов - лучше использовать regex
            if(!fileType.equals(".zip") && !fileType.equals(".rar"))
            {
                return new ValidateResponse(false, "files", "Формат файла должен быть zip или rar");
            }
        }
        return new ValidateResponse(true, "", "");
    }

}