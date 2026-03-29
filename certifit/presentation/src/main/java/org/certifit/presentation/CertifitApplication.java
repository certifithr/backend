package org.certifit.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CertifitApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertifitApplication.class, args);

        System.out.println("Hello World");
    }
}
