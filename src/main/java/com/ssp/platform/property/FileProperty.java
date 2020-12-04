package com.ssp.platform.property;

import com.ssp.platform.response.ValidatorResponse;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Data
@ConfigurationProperties(prefix = "file")
public class FileProperty {
    private String uploadDirectory;
    private String[] restrictedTypes;
}
