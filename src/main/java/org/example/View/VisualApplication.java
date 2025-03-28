package org.example.View;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VisualApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(VisualApplication.class, args);
        System.out.println();
    }


    @Override
    public void run(String[] args) throws Exception {
    }
}
