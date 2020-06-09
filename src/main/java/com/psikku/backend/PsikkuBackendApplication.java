package com.psikku.backend;

import com.psikku.backend.config.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class})
public class PsikkuBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsikkuBackendApplication.class, args);
    }

}
