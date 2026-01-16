package com.github.oscarmgh.deskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.github.oscarmgh.deskflow.config.DotEnvLoader;

@SpringBootApplication
@EnableScheduling
public class DeskflowApplication {

	public static void main(String[] args) {
		DotEnvLoader.loadDotEnvIfNotInProduction(args);
		SpringApplication.run(DeskflowApplication.class, args);
	}

}
