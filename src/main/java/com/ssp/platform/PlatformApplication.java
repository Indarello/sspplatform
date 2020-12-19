package com.ssp.platform;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@SpringBootApplication
public class PlatformApplication {

	private static final Logger logger = Logger.getLogger(PlatformApplication.class);

	/**private static PurchaseService purchaseService;

    public PlatformApplication(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }*/

    public static void main(String[] args) throws IOException {
        //Загрузка настроек логера из log4j.properties
		Properties props = new Properties();
		props.load(new FileInputStream("./src/main/resources/log4j.properties"));
		PropertyConfigurator.configure(props);

		logger.info("Starting application..");

		try {

			SpringApplication.run(PlatformApplication.class, args);
		}catch (Exception ex){
			String message = ex.getMessage() == null?"Application failed... StackTrace:\n":ex.getMessage();
			logger.error( message + Arrays.toString(ex.getStackTrace()), ex);
		}

        /**try{
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new SSPPlatformBot(purchaseService));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/

	}
	//System.out.println("123");  для копирования
}
