package com.psikku.backend;

import com.psikku.backend.config.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class})
@EnableScheduling
public class PsikkuBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsikkuBackendApplication.class, args);
    }

}
