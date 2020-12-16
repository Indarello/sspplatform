package com.ssp.platform.logging;

import com.ssp.platform.entity.User;
import com.ssp.platform.logging.Service.LogService;
import com.ssp.platform.property.FileProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

//Формат лога:
//время [роль/username] контроллер: тип_действия [успешно выполнено ? : ошибка]
@Component
public class Log {
    public static final String USER_GUEST = "guest";

    public static final String CONTROLLER_FILE = "File";
    public static final String CONTROLLER_PURCHASE = "Purchase";
    public static final String CONTROLLER_QA = "QA";
    public static final String CONTROLLER_SUPPLY = "Supply";
    public static final String CONTROLLER_USER = "User";

    private static final String mask = "%s [%s/%s] %s: %s";
    private static final String dateMask = "yyyy-MM-dd HH:mm:ss:S";

    private final LogFile logFile;
    private final LogService logService;

    @Autowired
    public Log(LogFile logFile, LogService logService) {
        this.logFile = logFile;
        this.logService = logService;
    }

    public void info(User user, String controller, String action) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateMask);
        Date infoDate = new Date();

        String line = String.format(mask, dateFormat.format(infoDate), user.getRole(), user.getUsername(), controller, action);

        System.out.println(line);
        logFile.put(line);

        LogEntity logEntity = new LogEntity();
        logEntity.setDate(Timestamp.from(infoDate.toInstant()));
        logEntity.setUsername(user.getUsername());
        logEntity.setRole(user.getRole());
        logEntity.setActionController(controller);
        logEntity.setActionType(action);
        logEntity.setActionSucceed(true);

        logService.put(logEntity);
    }
}
