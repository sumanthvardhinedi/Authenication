package dev.sumanth.manual_authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ManualAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManualAuthenticationApplication.class, args);
    }

}
