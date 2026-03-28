package org.certifit.presentation;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "org.certifit.presentation",
        "org.certifit.application",
        "org.certifit.db"
})
@EnableJpaRepositories(basePackages = "org.certifit.db.repository")
@EntityScan(basePackages = "org.certifit.db.entity")
public class CertifitApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(CertifitApplication.class, args);

        System.out.println("Hello World");
    }
}
