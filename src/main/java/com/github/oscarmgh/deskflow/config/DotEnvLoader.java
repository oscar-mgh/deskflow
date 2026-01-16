package com.github.oscarmgh.deskflow.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Arrays;

public class DotEnvLoader {

    public static void loadDotEnvIfNotInProduction(String[] args) {
        boolean isProd = Arrays.asList(args).contains("--spring.profiles.active=prod") ||
                         Arrays.asList(args).contains("--spring.profiles.active=production");

        if (!isProd) {
            Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .ignoreIfMalformed()
                .load();

            System.setProperty("CLOUDINARY_CLOUD_NAME", dotenv.get("CLOUDINARY_CLOUD_NAME"));
            System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
            System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
        }
    }
}