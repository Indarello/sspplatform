package com.ssp.platform.property;

        import lombok.Data;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "email-announcement")
public class EmailAnnouncementProperty {
    private int status;
    private String host;
    private String heading;
    private String firstLine;
    private int description;
    private int budget;
}
