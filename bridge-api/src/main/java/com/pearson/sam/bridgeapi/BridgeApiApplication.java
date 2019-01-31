package com.pearson.sam.bridgeapi;

import java.util.Map.Entry;
import java.util.Properties;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.pearson.sam.bridgeapi.config.SecretManagerCommon;

@SpringBootApplication(exclude = { ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class })
@ServletComponentScan
public class BridgeApiApplication extends SpringBootServletInitializer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(BridgeApiApplication.class);

	public static void main(String[] args) throws ParseException {

		SpringApplication app = new SpringApplication(BridgeApiApplication.class);

		String profile = System.getProperty("env");

		//logger.info("profile :" + profile);

		if (profile != null && !"Local".equalsIgnoreCase(profile)) {
			SecretManagerCommon.getSecret();
			Properties defaultProperties = new Properties();

			for (Entry<String, Object> entry : SecretManagerCommon.secretMap.entrySet()) {
				String[] str = entry.getKey().split("\\.");
				if (str[0].equalsIgnoreCase(profile)) {
					defaultProperties.put(entry.getKey().substring(profile.length() + 1), entry.getValue().toString());
				}
			}
			app.setDefaultProperties(defaultProperties);
		}
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
