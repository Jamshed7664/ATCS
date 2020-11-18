package com.nxtlife.efkon.enforcementconfigurator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import com.nxtlife.efkon.enforcementconfigurator.config.FileStorageProperties;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties({ FileStorageProperties.class })
public class EnforcementConfiguratorApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EnforcementConfiguratorApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(EnforcementConfiguratorApplication.class);
	}

}
