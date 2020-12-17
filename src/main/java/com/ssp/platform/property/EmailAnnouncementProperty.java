package com.ssp.platform.property;

        import lombok.Data;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.stereotype.Component;

        import java.nio.charset.StandardCharsets;

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

    public String getHost()
    {
        return new String(host.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getHeading()
    {
        return new String(heading.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String getFirstLine()
    {
        return new String(firstLine.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }


}
