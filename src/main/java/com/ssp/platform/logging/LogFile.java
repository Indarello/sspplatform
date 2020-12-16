package com.ssp.platform.logging;

import com.ssp.platform.property.FileProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class LogFile {
    private static final String LOG_TYPE = "log";
    private static final String BACKUP_TYPE = "backup";

    private static final String fileDateMask = "yyyyMMdd";
    private static final String fileNameMask = "log-%s.%s";

    private FileProperty fileProperty;
    private final Path logStorageLocation;
    private Path logLocation;
    private Path backupLocation;

    @Autowired
    public LogFile(FileProperty fileProperty) throws IOException {
        this.fileProperty = fileProperty;

        String directory = fileProperty.getLogDirectory();
        if(directory.contains(":")) logStorageLocation = Paths.get(directory);
        else logStorageLocation = Paths.get(directory).toAbsolutePath().normalize();
        if (!Files.exists(logStorageLocation)) {
            Files.createDirectory(logStorageLocation);
        }
    }

    public void put(String line) throws IOException {
        line += "\n";

        SimpleDateFormat dateFormat = new SimpleDateFormat(fileDateMask);
        String fileName = String.format(fileNameMask, dateFormat.format(new Date()), LOG_TYPE);
        String backupFileName = String.format(fileNameMask, dateFormat.format(new Date()), BACKUP_TYPE);

        String logPath = fileProperty.getLogDirectory() + "\\" + fileName;
        String backupPath = fileProperty.getLogDirectory() + "\\" + backupFileName;
        if(logPath.contains(":")) {
            logLocation = Paths.get(logPath);
            backupLocation = Paths.get(backupPath);
        } else {
            logLocation = Paths.get(logPath).toAbsolutePath().normalize();
            backupLocation = Paths.get(backupPath).toAbsolutePath().normalize();
        }

        if (!Files.exists(logLocation)){
            Files.createFile(logLocation);
        }

        Files.copy(logLocation, backupLocation, StandardCopyOption.REPLACE_EXISTING);
        Files.write(logLocation, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }
}
