package com.ssp.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

@SpringBootApplication
public class PlatformApplication {

//	private static java.util.logging.Logger logger;
	private static final Logger logger = Logger.getLogger(PlatformApplication.class);


	public static void main(String[] args) throws IOException {
		//Попытки загрущить настройки для JUL
//		System.out.println(System.getProperty("java.util.logging.config.file"));
//		System.setProperty("java.util.logging.config.file",
//				".\\src\\main\\java\\resorces\\log.properties");

		//Успешная загрузка настроек JUL
//		String path = PlatformApplication.class.getClassLoader()
//												.getResource("log.properties")
//												.getFile();
//		System.setProperty("java.util.logging.config.file", path);
//		logger = java.util.logging.Logger.getLogger(PlatformApplication.class.getName());


		//Успешное решение, но без даты
//		FileHandler fh = new FileHandler("./log/log.log", true);
//		fh.setFormatter(new SimpleFormatter());
//		fh.setLevel(Level.ALL);
//		logger.addHandler(fh);



		PropertyConfigurator.configure("log4j.properties");
		logger.debug("Sample debug message");
		logger.info("Sample info message");
		logger.warn("Sample warn message");
		logger.error("Sample error message");
		logger.fatal("Sample fatal message");

		logger.info("Starting application..");

		try {
			SpringApplication.run(PlatformApplication.class, args);
		}catch (Exception ex){
			String message = ex.getMessage() == null?"Empty message":ex.getMessage();
			logger.error( message, ex);
		}

	}
	//System.out.println("123");  для копирования
}
